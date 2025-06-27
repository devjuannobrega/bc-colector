import sqlite3
from werkzeug.security import generate_password_hash, check_password_hash
from app.database import DB_CONFIG


def buscar_usuario_ativo(usuario):
    conn = sqlite3.connect(DB_CONFIG)
    cur = conn.cursor()
    cur.execute("SELECT senha FROM usuarios WHERE usuario = ? AND ativo = 1", (usuario,))
    row = cur.fetchone()
    conn.close()
    return row


def criar_usuario(nome, usuario, senha):
    senha_hash = generate_password_hash(senha)
    conn = sqlite3.connect(DB_CONFIG)
    cur = conn.cursor()
    try:
        cur.execute("INSERT INTO usuarios (nome, usuario, senha, ativo) VALUES (?, ?, ?, 1)",
                    (nome, usuario, senha_hash))
        conn.commit()
        return True
    except sqlite3.IntegrityError:
        return False
    finally:
        conn.close()


def atualizar_senha_usuario(usuario, nova_senha):
    senha_hash = generate_password_hash(nova_senha)
    conn = sqlite3.connect(DB_CONFIG)
    cur = conn.cursor()
    cur.execute("UPDATE usuarios SET senha = ? WHERE usuario = ?", (senha_hash, usuario))
    conn.commit()
    conn.close()


def desativar_usuario(usuario):
    conn = sqlite3.connect(DB_CONFIG)
    cur = conn.cursor()
    cur.execute("UPDATE usuarios SET ativo = 0 WHERE usuario = ?", (usuario,))
    conn.commit()
    conn.close()


def alterar_login_usuario(usuario_antigo, novo_login):
    conn = sqlite3.connect(DB_CONFIG)
    cur = conn.cursor()
    cur.execute("UPDATE usuarios SET usuario = ? WHERE usuario = ?", (novo_login, usuario_antigo))
    conn.commit()
    conn.close()


def buscar_usuarios():
    conn = sqlite3.connect(DB_CONFIG)
    conn.row_factory = sqlite3.Row
    cur = conn.cursor()
    cur.execute("SELECT nome, usuario, ativo FROM usuarios")
    usuarios = cur.fetchall()
    conn.close()
    return usuarios


def validar_senha_admin(senha_informada):
    conn = sqlite3.connect(DB_CONFIG)
    cur = conn.cursor()
    cur.execute("SELECT valor FROM seguranca WHERE chave = 'senha_admin'")
    senha_real = cur.fetchone()[0]
    conn.close()
    return senha_informada == senha_real


def validar_senha_reimpressao(senha_informada):
    conn = sqlite3.connect(DB_CONFIG)
    cur = conn.cursor()
    cur.execute("SELECT valor FROM seguranca WHERE chave = 'senha_reimpressao'")
    senha_real = cur.fetchone()[0]
    conn.close()
    return senha_informada == senha_real


def alterar_senha_admin(nova):
    conn = sqlite3.connect(DB_CONFIG)
    cur = conn.cursor()
    cur.execute("UPDATE seguranca SET valor = ? WHERE chave = 'senha_admin'", (nova,))
    conn.commit()
    conn.close()


def alterar_senha_reimpressao(nova):
    conn = sqlite3.connect(DB_CONFIG)
    cur = conn.cursor()
    cur.execute("UPDATE seguranca SET valor = ? WHERE chave = 'senha_reimpressao'", (nova,))
    conn.commit()
    conn.close()
