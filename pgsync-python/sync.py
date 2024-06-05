# import psycopg2
# import requests
# import logging
# import json
# import time
# from psycopg2.extras import DictCursor
# from requests.exceptions import HTTPError
#
# # Configure logging
# logging.basicConfig(level=logging.DEBUG)
# logger = logging.getLogger(__name__)
#
# def connect_to_postgres():
#     """Establishes a PostgreSQL connection."""
#     return psycopg2.connect(
#         dbname="user_db",
#         user="postgres",
#         password="qwer",
#         host="136.228.158.126",  # Using Docker service name
#         port="3286"
#     )
#
# def connect_to_elasticsearch():
#     """Returns Elasticsearch bulk URL and headers."""
#     return 'http://elasticsearch:9200/user/_bulk', {"Content-Type": "application/x-ndjson"}
#
# def fetch_all_data(pg_conn):
#     """Fetch all user details from PostgreSQL."""
#     try:
#         with pg_conn.cursor(cursor_factory=DictCursor) as cursor:
#             cursor.execute("""
#                 SELECT * FROM user_tbl
#                 ORDER BY user_tbl.id ASC  -- Ensuring a consistent order
#             """)
#             return cursor.fetchall()
#     except Exception as e:
#         logger.error("Error fetching data from PostgreSQL: %s", e)
#         raise
#
# def format_data(rows):
#     """Formats rows into a bulk JSON action list for Elasticsearch."""
#     actions = []
#     for row in rows:
#         action = {
#             "index": {
#                 "_index": "user",
#                 "_id": row['id']
#             }
#         }
#         doc = {key: row[key] for key in row.keys() if key != 'id'}
#         actions.append(json.dumps(action))
#         actions.append(json.dumps(doc))
#     return "\n".join(actions) + "\n"
#
# def sync_data(pg_conn, es_url, headers):
#     """Synchronizes data from PostgreSQL to Elasticsearch."""
#     try:
#         rows = fetch_all_data(pg_conn)
#         if not rows:
#             logger.debug("No new data to sync.")
#             return
#
#         bulk_data = format_data(rows)
#         response = requests.post(es_url, headers=headers, data=bulk_data)
#         response.raise_for_status()
#         logger.debug("Data successfully synchronized to Elasticsearch.")
#     except HTTPError as e:
#         logger.error("HTTP error during sync: %s", e.response.text)
#         raise
#     except Exception as e:
#         logger.error("Error during sync: %s", e)
#         raise
#
# if __name__ == "__main__":
#     pg_conn = connect_to_postgres()
#     es_url, headers = connect_to_elasticsearch()
#     try:
#         while True:
#             sync_data(pg_conn, es_url, headers)
#             time.sleep(10)  # Poll every 10 seconds
#     finally:
#         pg_conn.close()
#
# #
# #
# # import psycopg2
# # import requests
# # import logging
# # import json
# # import time
# # from psycopg2.extras import DictCursor
# # from requests.exceptions import HTTPError
# #
# # # Configure logging
# # logging.basicConfig(level=logging.DEBUG)
# # logger = logging.getLogger(__name__)
# #
# # def connect_to_postgres():
# #     """Establishes a PostgreSQL connection."""
# #     return psycopg2.connect(
# #         dbname="user_db",
# #         user="postgres",
# #         password="qwer",
# #         host="136.228.158.126",  # Using Docker service name
# #         port="3286"
# #     )
# #
# # def connect_to_elasticsearch():
# #     """Returns Elasticsearch bulk URL and headers."""
# #     return 'http://elasticsearch:9200/user/_bulk', {"Content-Type": "application/x-ndjson"}
# #
# # def fetch_all_data(pg_conn):
# #     """Fetch all user details from PostgreSQL."""
# #     try:
# #         with pg_conn.cursor(cursor_factory=DictCursor) as cursor:
# #             cursor.execute("""
# #                 SELECT * FROM user_tbl
# #                 WHERE deleted_at IS NULL
# #                 ORDER BY user_tbl.id ASC  -- Ensuring a consistent order
# #             """)
# #             return cursor.fetchall()
# #     except Exception as e:
# #         logger.error("Error fetching data from PostgreSQL: %s", e)
# #         raise
# #
# #
# # def format_data(rows):
# #     """Formats rows into a bulk JSON action list for Elasticsearch."""
# #     actions = []
# #     for row in rows:
# #         if row['deleted_at'] is None:
# #             action = {
# #                 "index": {
# #                     "_index": "user",
# #                     "_id": row['id']
# #                 }
# #             }
# #             doc = {key: row[key] for key in row.keys() if key != 'id' and key != 'deleted_at'}
# #             actions.append(json.dumps(action))
# #             actions.append(json.dumps(doc))
# #         else:
# #             action = {
# #                 "delete": {
# #                     "_index": "user",
# #                     "_id": row['id']
# #                 }
# #             }
# #             actions.append(json.dumps(action))
# #     return "\n".join(actions) + "\n"
# #
# # def sync_data(pg_conn, es_url, headers):
# #     """Synchronizes data from PostgreSQL to Elasticsearch."""
# #     try:
# #         rows = fetch_all_data(pg_conn)
# #         if not rows:
# #             logger.debug("No new data to sync.")
# #             return
# #
# #         bulk_data = format_data(rows)
# #         response = requests.post(es_url, headers=headers, data=bulk_data)
# #         response.raise_for_status()
# #         logger.debug("Data successfully synchronized to Elasticsearch.")
# #     except HTTPError as e:
# #         logger.error("HTTP error during sync: %s", e.response.text)
# #         raise
# #     except Exception as e:
# #         logger.error("Error during sync: %s", e)
# #         raise
# #
# # if __name__ == "__main__":
# #     pg_conn = connect_to_postgres()
# #     es_url, headers = connect_to_elasticsearch()
# #     try:
# #         while True:
# #             sync_data(pg_conn, es_url, headers)
# #             time.sleep(10)  # Poll every 10 seconds
# #     finally:
# #         pg_conn.close()


import psycopg2
import requests
import logging
import json
import time
from psycopg2.extras import DictCursor
from requests.exceptions import HTTPError

# Configure logging
logging.basicConfig(level=logging.DEBUG)
logger = logging.getLogger(__name__)

def connect_to_postgres():
    """Establishes a PostgreSQL connection."""
    return psycopg2.connect(
        dbname="user_db",
                user="postgres",
                password="qwer",
                host="136.228.158.126",  # Using Docker service name
                port="3286"
    )

def connect_to_elasticsearch():
    """Returns Elasticsearch bulk URL and headers."""
    return 'http://136.228.158.126:3125/user/_bulk', {"Content-Type": "application/x-ndjson"}

def fetch_all_data(pg_conn):
    """Fetch all user details from PostgreSQL."""
    try:
        with pg_conn.cursor(cursor_factory=DictCursor) as cursor:
            cursor.execute("""
                SELECT * FROM user_tbl              
            ORDER BY user_tbl.id ASC  -- Ensuring a consistent order
   
            """)
            return cursor.fetchall()
    except Exception as e:
        logger.error("Error fetching data from PostgreSQL: %s", e)
        raise

def fetch_elasticsearch_ids():
    """Fetch all document IDs from Elasticsearch."""
    response = requests.get('http://136.228.158.126:3125/user/_search', json={
        "query": {
            "match_all": {}
        },
        "_source": False
    })
    hits = response.json().get('hits', {}).get('hits', [])
    return {hit['_id'] for hit in hits}

def format_data(rows):
    """Formats rows into a bulk JSON action list for Elasticsearch."""
    actions = []
    for row in rows:
        action = {
            "index": {
                "_index": "user",
                "_id": row['id']
            }
        }
        doc = {key: row[key] for key in row.keys() if key != 'id'}
        actions.append(json.dumps(action))
        actions.append(json.dumps(doc))
    return "\n".join(actions) + "\n"

def sync_data(pg_conn, es_url, headers):
    """Synchronizes data from PostgreSQL to Elasticsearch."""
    try:
        rows = fetch_all_data(pg_conn)
        if not rows:
            logger.debug("No new data to sync.")
            return

        es_ids = fetch_elasticsearch_ids()
        pg_ids = {str(row['id']) for row in rows}

        # Detect deletions
        ids_to_delete = es_ids - pg_ids

        bulk_data = format_data(rows)

        for id_to_delete in ids_to_delete:
            bulk_data += json.dumps({"delete": {"_index": "user", "_id": id_to_delete}}) + "\n"

        response = requests.post(es_url, headers=headers, data=bulk_data)
        response.raise_for_status()
        logger.debug("Data successfully synchronized to Elasticsearch.")
    except HTTPError as e:
        logger.error("HTTP error during sync: %s", e.response.text)
        raise
    except Exception as e:
        logger.error("Error during sync: %s", e)
        raise

if __name__ == "__main__":
    pg_conn = connect_to_postgres()
    es_url, headers = connect_to_elasticsearch()
    try:
        while True:
            sync_data(pg_conn, es_url, headers)
            time.sleep(10)  # Poll every 10 seconds
    finally:
        pg_conn.close()
