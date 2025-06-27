import sqlite3
import os
import csv
from datetime import datetime
from app.database import DB_ETIQUETAS, CSV_PATH

def ja_impresso(pedido, chave):
    conn = sqlite3.connect(DB_ETIQUETAS)
    cur = conn.cursor()
    cur.execute("SELECT * FROM impressoes WHERE pedido = ? AND chave_acesso = ?", (pedido, chave))
    result = cur.fetchone()
    conn.close()
    return result

def registrar_impressao(pedido, chave, url, usuario):
    agora = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    impresso = ja_impresso(pedido, chave)
    primeira, segunda = "", ""
    if impresso is None:
        primeira = agora
    else:
        segunda = agora

    conn = sqlite3.connect(DB_ETIQUETAS)
    cur = conn.cursor()
    cur.execute("""
        INSERT INTO impressoes (pedido, chave_acesso, url_etiqueta, usuario, data_hora)
        VALUES (?, ?, ?, ?, ?)
    """, (pedido, chave, url, usuario, agora))
    conn.commit()
    conn.close()

    if not os.path.exists(CSV_PATH):
        with open(CSV_PATH, 'w', newline='', encoding='utf-8') as f:
            writer = csv.writer(f)
            writer.writerow(["ID B4X", "Usuário", "Data/Hora 1ª Impressão", "Data/Hora Reimpressão"])

    with open(CSV_PATH, 'a', newline='', encoding='utf-8') as csvfile:
        writer = csv.writer(csvfile)
        writer.writerow([pedido, usuario, primeira, segunda])

def imprimir_lpt1(url):
    import requests
    local_filename = os.path.join(os.getcwd(), "temp_etiqueta.pdf")
    with requests.get(url, stream=True) as r:
        with open(local_filename, 'wb') as f:
            for chunk in r.iter_content(chunk_size=8192):
                f.write(chunk)
    os.system(f'copy /b "{local_filename}" LPT1')
    os.remove(local_filename)
