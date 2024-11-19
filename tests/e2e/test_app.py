import requests

def test_health_check():
    response = requests.get("http://app:8080/health")
    assert response.status_code == 200

def test_database_connection():
    response = requests.get("http://app:8080/api/test-db")
    assert response.json()["status"] == "success"
