from recombee_api_client.api_client import RecombeeClient
from recombee_api_client.api_requests import *
import json
import sys

client = RecombeeClient('illinois-institue-of-tech-dev', 'igCzKWB3UwYFyn6B9NLalbywd9k9TmGOBiLtVZM3e80E0Djsmem55mt0kOYTiNPr')

#client = RecombeeClient('iitchicago-test4', 'SYaARkmc7KWE6kjZkESn3xRKozUQVFkhfdDPcUfHSvzWK75GDPdYULPMYFAl8qc1')

recommended = client.send(RecommendItemsToItem(sys.argv[1],sys.argv[2],5,return_properties=True))
print(recommended)
