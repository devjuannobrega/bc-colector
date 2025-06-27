from pystray import Icon as icon, Menu as menu, MenuItem as item
from PIL import Image
import os
import socket

def get_ip():
    import socket
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    try:
        s.connect(("8.8.8.8", 80))
        ip = s.getsockname()[0]
    finally:
        s.close()
    return ip

def iniciar_tray():
    def abrir_sistema(icon, item):
        os.system('start http://localhost:5000')

    def abrir_configuracoes(icon, item):
        os.system('start http://localhost:5000/config')

    def mostrar_url_host(icon, item):
        ip = get_ip()
        url = f"http://{ip}:5000"
        icon.notify(f"Acesse via coletor: {url}")

    def on_exit(icon, item):
        icon.stop()
        os._exit(0)

    image = Image.new('RGB', (64, 64), color=(255, 255, 0))
    tray_icon = icon("etiqueta", image, "Emissor de Etiquetas", menu(
        item('Acessar Sistema', abrir_sistema),
        item('Configurações', abrir_configuracoes),
        item('Mostrar URL do Host', mostrar_url_host),
        item('Sair', on_exit)
    ))
    tray_icon.run()
