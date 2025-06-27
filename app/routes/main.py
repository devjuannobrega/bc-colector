from flask import Blueprint, render_template, request, redirect, flash, session
from app.services.api_bcf import consultar_pedido_chave
from app.services.impressao import imprimir_lpt1, registrar_impressao, ja_impresso

bp = Blueprint('main', __name__)

@bp.route('/', methods=['GET', 'POST'])
def index():
    from app.database import DB_CONFIG
    import sqlite3
    conn = sqlite3.connect(DB_CONFIG)
    cur = conn.cursor()
    cur.execute("SELECT COUNT(*) FROM usuarios WHERE ativo = 1")
    usuarios_ativos = cur.fetchone()[0]
    conn.close()

    if usuarios_ativos > 0 and 'usuario' not in session:
        return redirect('/config/login')

    if request.method == 'POST':
        chave = request.form.get('chave', '').strip()
        if not chave or len(chave) != 44:
            flash("Chave de acesso inválida.")
            return redirect('/')

        resultado = consultar_pedido_chave(chave)
        if not resultado:
            flash("Pedido não encontrado ou erro na API.")
            return redirect('/')

        pedido, etiqueta, data_entrega, chave, status, descricao_status = resultado
        if status == 8:
            flash("PEDIDO CANCELADO", "cancelado")
            return redirect('/')

        from datetime import datetime
        if data_entrega:
            try:
                data_entrega_dt = datetime.strptime(data_entrega, "%Y-%m-%d").date()
                if data_entrega_dt > datetime.now().date():
                    flash(f"Etiqueta liberada somente em: {data_entrega_dt.strftime('%d/%m/%Y')}")
                    return redirect('/')
            except Exception:
                flash("Data de liberação inválida.")
                return redirect('/')

        if not etiqueta:
            flash("Etiqueta ainda não está disponível.")
            return redirect('/')

        if ja_impresso(pedido, chave):
            session['dados'] = {'pedido': pedido, 'etiqueta': etiqueta, 'chave': chave}
            return redirect('/reimpressao')

        usuario = session.get('usuario', 'servidor_web')
        imprimir_lpt1(etiqueta)
        registrar_impressao(pedido, chave, etiqueta, usuario)
        flash("Etiqueta enviada para impressão.")
        return redirect('/')

    return render_template('index.html')


@bp.route('/reimpressao', methods=['GET', 'POST'])
def reimpressao():
    if 'dados' not in session:
        return redirect('/')

    if request.method == 'POST':
        senha = request.form.get('senha')
        from app.database import DB_CONFIG
        import sqlite3
        conn = sqlite3.connect(DB_CONFIG)
        cur = conn.cursor()
        cur.execute("SELECT valor FROM seguranca WHERE chave = 'senha_reimpressao'")
        senha_real = cur.fetchone()[0]
        conn.close()

        if senha != senha_real:
            flash("Senha incorreta. Reimpressão não autorizada.")
            return redirect('/reimpressao')

        dados = session.pop('dados')
        usuario = session.get('usuario', 'servidor_web')
        imprimir_lpt1(dados['etiqueta'])
        registrar_impressao(dados['pedido'], dados['chave'], dados['etiqueta'], usuario)
        flash("Etiqueta reimpressa com sucesso.")
        return redirect('/')

    return render_template('reimpressao.html')