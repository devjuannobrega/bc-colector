from flask import Flask
from .database import init_db
from .routes import main, config

def create_app():
    app = Flask(__name__)
    app.secret_key = 'etiqueta-bcf'
    app.permanent_session_lifetime = 300  # segundos (5 minutos)

    init_db()

    app.register_blueprint(main.bp)
    app.register_blueprint(config.bp, url_prefix='/templates')

    return app
