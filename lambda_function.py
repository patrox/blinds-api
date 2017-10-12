import urllib.request
import json

def lambda_handler(event, context):
    request = urllib.request.Request('http://6b606e05.ngrok.io/blinds/control/all/open', headers={'Content-type': 'application/json'})
    response = json.load(urllib.request.urlopen(request))

    print(response)
    return response
