from recombee_api_client.api_client import RecombeeClient
from recombee_api_client.api_requests import *
import json
import sys

client = RecombeeClient('illinois-institue-of-tech-dev', 'igCzKWB3UwYFyn6B9NLalbywd9k9TmGOBiLtVZM3e80E0Djsmem55mt0kOYTiNPr')

#client = RecombeeClient('iitchicago-test4', 'SYaARkmc7KWE6kjZkESn3xRKozUQVFkhfdDPcUfHSvzWK75GDPdYULPMYFAl8qc1')


requests = []
file=sys.argv[1]
with open(file+"items.json") as f:
	data = json.loads(f.read())
	for item_id,values in data.items():
		r = SetItemValues(item_id,
				values,
				cascade_create=True)
		requests.append(r)

res = client.send(Batch(requests))
print(res)

