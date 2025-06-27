from flask import Blueprint, render_template, request, redirect, flash, session
import sqlite3
from werkzeug.security import generate_password_hash
from app.database import DB_CONFIG

bp = Blueprint('templates', __name__)

@bp.route('/login', methods=['GET', 'POST'])
def login():
    if request.method == 'POST':
        usuario = request.form['usuario']
        senha = request.form['senha']

        conn = sqlite3.connect(DB_CONFIG)
        cur = conn.cursor()
        cur.execute("SELECT senha FROM usuarios WHERE usuario = ? AND ativo = 1", (usuario,))
        row = cur.fetchone()
        conn.close()

        from werkzeug.security import check_password_hash
        if row and check_password_hash(row[0], senha):
            session.permanent = True
            session['usuario'] = usuario
            return redirect('/')
        flash("Login inválido ou usuário inativo.")
    return render_template('login.html')


#metodo que registra o log de horario e limpa a seção
@bp.route('/logout', methods=['POST'])
def logout():
    session.clear()
    return redirect('/templates/login')

@bp.route('/', methods=['GET', 'POST'])
def config():
    if request.method == 'POST':
        senha = request.form.get('senha')
        conn = sqlite3.connect(DB_CONFIG)
        cur = conn.cursor()
        cur.execute("SELECT valor FROM seguranca WHERE chave = 'senha_admin'")
        senha_correta = cur.fetchone()[0]
        conn.close()

        if senha == senha_correta:
            session['admin'] = True
            return render_template('configuracoes.html')
        else:
            flash("Senha de administrador incorreta.")
            return redirect('/templates')
    return render_template('senha_admin.html')

@bp.route('/novo_usuario', methods=['GET', 'POST'])
def novo_usuario():
    if request.method == 'POST':
        nome = request.form['nome']
        usuario = request.form['usuario']
        senha = generate_password_hash(request.form['senha'])
        conn = sqlite3.connect(DB_CONFIG)
        cur = conn.cursor()
        try:
            cur.execute("INSERT INTO usuarios (nome, usuario, senha, ativo) VALUES (?, ?, ?, 1)", (nome, usuario, senha))
            conn.commit()
            flash("Usuário criado com sucesso.")
        except sqlite3.IntegrityError:
            flash("Usuário já existe.")
        conn.close()
        return redirect('/templates')
    return render_template('novo_usuario.html')
