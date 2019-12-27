from recombee_api_client.api_client import RecombeeClient
from recombee_api_client.api_requests import *
import json
import sys

client = RecombeeClient('illinois-institue-of-tech-dev', 'igCzKWB3UwYFyn6B9NLalbywd9k9TmGOBiLtVZM3e80E0Djsmem55mt0kOYTiNPr')

#client = RecombeeClient('iitchicago-test4', 'SYaARkmc7KWE6kjZkESn3xRKozUQVFkhfdDPcUfHSvzWK75GDPdYULPMYFAl8qc1')

userId=sys.argv[1]
itemId=sys.argv[2]
rating=int(sys.argv[3])-2
print(rating)
result = client.send(AddRating(userId, itemId, rating, cascade_create=True))
print(result)