import MySQLdb

try:
    db = MySQLdb.connect(
        host="127.0.0.1",
        port=3306,  # Make sure this matches your MySQL port
        user="root",
        passwd="project",
        db="mc"
    )
    print("Connected to MySQL successfully!")

    db_cursor = db.cursor()
    db_cursor.execute("SHOW TABLES")
    rows = db_cursor.fetchall()
    for row in rows:
        print(row)
    db.close()
except Exception as e:
    print("Error connecting to MySQL:", e)
