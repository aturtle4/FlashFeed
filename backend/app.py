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


@app.route('/user/register', methods=['POST'])
def register():
    if request.method == 'POST':
        data = request.get_json()
        name = data['name']
        email = data['email']
        password = data['password']
        cursor = mysql.cursor()
        cursor.execute("INSERT INTO users (name, email, password) VALUES (%s, %s, %s)", (name, email, password))
        mysql.commit()
        cursor.close()
        return jsonify({"message": "User registered successfully"}), 201
    else:
        return jsonify({"error": "Method not allowed"}), 405

@app.route('/user/login', methods=['POST'])
def login():
    if request.method == 'POST':
        data = request.get_json()
        email = data['email']
        password = data['password']
        cursor = mysql.cursor()
        cursor.execute("SELECT * FROM users WHERE email = %s AND password = %s", (email, password))
        user = cursor.fetchone()
        cursor.close()
        if user:
            return jsonify({"message": "Login successful"}), 200
        else:
            return jsonify({"error": "Invalid credentials"}), 401
    else:
        return jsonify({"error": "Method not allowed"}), 405

@app.route('/news')
def news():
    if request.method == 'GET':
        category = request.args.get("category")
        if not category:
            return jsonify({
                "error": "please add category in query params"
            }), 404
        return jsonify(getNews(category)), 200

@app.route('/user/save', methods=['POST'])
def save():
    if request.method == 'POST':
        data = request.get_json()
        news_id = data['news_id']
        user_id = data['user_id']
        cursor = mysql.cursor()
        cursor.execute("INSERT INTO user_saved_articles (news_id, user_id) VALUES (%s, %s)", (news_id, user_id))
        mysql.commit()
        cursor.close()
        return jsonify({"message": "News saved successfully"}), 201
    else:
        return jsonify({"error": "Method not allowed"}), 405
    
@app.route('/user/saved', methods=['GET'])
def saved():
    if request.method == 'GET':
        user_id = request.args.get("user_id")
        cursor = mysql.cursor()
        cursor.execute("SELECT * FROM user_saved_articles WHERE user_id = %s", (user_id,))
        user_saved_articles = cursor.fetchall()
        cursor.close()
        return jsonify({"user_saved_articles": user_saved_articles}), 200
    else:
        return jsonify({"error": "Method not allowed"}), 405
    
@app.route('/user/delete', methods=['POST'])
def delete():
    if request.method == 'POST':
        data = request.get_json()
        news_id = data['news_id']
        user_id = data['user_id']
        cursor = mysql.cursor()
        cursor.execute("DELETE FROM user_saved_articles WHERE news_id = %s AND user_id = %s", (news_id, user_id))
        mysql.commit()
        cursor.close()
        return jsonify({"message": "News deleted successfully"}), 200
    else:
        return jsonify({"error": "Method not allowed"}), 405

if __name__ == '__main__':
    app.debug = True
    app.run(host='0.0.0.0',port=5000,use_reloader=True)
