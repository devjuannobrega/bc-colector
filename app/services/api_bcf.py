import requests

def consultar_pedido_chave(chave):
    numero_nota = chave[25:34]
    url = f"https://www.bcferramentaria.com.br/api/v2/pedidos/nota-fiscal/{numero_nota}"
    auth = ("bc-producao", "XGts1djny4kU5mEIhgwNkF9fD0XeBF")
    response = requests.get(url, auth=auth)
    if response.status_code == 200:
        data = response.json()
        if not data:
            return None
        pedido_info = data[0]
        entrega = pedido_info.get("entregas", [{}])[0]
        return (
            str(pedido_info.get("codigo")),
            entrega.get("etiqueta"),
            entrega.get("data_entrega"),
            chave,
            pedido_info.get("status"),
            pedido_info.get("descricao_status")
        )
    return None
