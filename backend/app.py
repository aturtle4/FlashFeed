from flask import Flask, request, jsonify
from inshorts import getNews
from flask_cors import CORS
import MySQLdb

app = Flask(__name__)
app.secret_key = "i_am_not_feeling_sleepy_so_i_am_coding_this"
CORS(app)
app.config['CORS_HEADERS'] = 'Content-Type'

mysql = MySQLdb.connect(
        host="127.0.0.1",
        port=3306,  # Make sure this matches your MySQL port
        user="root",
        passwd="project",
        db="mc"
    )


@app.route('/news')
def news():
    if request.method == 'GET':
        category = request.args.get("category")
        if not category:
            return jsonify({
                "error": "please add category in query params"
            }), 404
        return jsonify(getNews(category)), 200


if __name__ == '__main__':
    app.debug = True
    app.run(host='0.0.0.0',port=5000,use_reloader=True)
