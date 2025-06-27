import sqlite3
import os
from werkzeug.security import generate_password_hash

#revisar ip para buscar dentro do app
BASE_DIR = r"\\10.100.10.20\bc-ferramentaria\E-Commerce\emissor de etiqueta"
DB_ETIQUETAS = os.path.join(BASE_DIR, "etiquetas.db")
DB_CONFIG = os.path.join(BASE_DIR, "configuracoes.db")
CSV_PATH = os.path.join(BASE_DIR,"log_impressoes.csv")

def init_db():
    conn = sqlite3.connect(DB_ETIQUETAS)
    cur = conn.cursor()
    cur.execute("""
        CREATE TABLE IF NOT EXISTS impressoes (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            pedido TEXT,
            chave_acesso TEXT,
            url_etiqueta TEXT,
            usuario TEXT,
            data_hora TEXT
        )
    """)
    conn.commit()
    conn.close()

    conn = sqlite3.connect(DB_CONFIG)
    cur = conn.cursor()
    cur.execute("""
        CREATE TABLE IF NOT EXISTS usuarios (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            nome TEXT,
            usuario TEXT UNIQUE,
            senha TEXT,
            ativo INTEGER DEFAULT 0
        )
    """)
    cur.execute("""
        CREATE TABLE IF NOT EXISTS seguranca (
            chave TEXT PRIMARY KEY,
            valor TEXT
        )
    """)
    cur.execute("INSERT OR IGNORE INTO seguranca (chave, valor) VALUES (?, ?)", ("senha_admin", "130981"))
    cur.execute("INSERT OR IGNORE INTO seguranca (chave, valor) VALUES (?, ?)", ("senha_reimpressao", "15975300"))
    cur.execute("INSERT OR IGNORE INTO usuarios (nome, usuario, senha, ativo) VALUES (?, ?, ?, 1)",
                ("Administrador", "admin", generate_password_hash("admin"),))
    conn.commit()
    conn.close()