
from recombee_api_client.api_client import RecombeeClient
from recombee_api_client.api_requests import *
import json
import sys

client = RecombeeClient('illinois-institue-of-tech-dev', 'igCzKWB3UwYFyn6B9NLalbywd9k9TmGOBiLtVZM3e80E0Djsmem55mt0kOYTiNPr')

#client = RecombeeClient('iitchicago-test4', 'SYaARkmc7KWE6kjZkESn3xRKozUQVFkhfdDPcUfHSvzWK75GDPdYULPMYFAl8qc1')

sport = sys.argv[2];
week=sys.argv[3];
city =sys.argv[4];
postcode= sys.argv[5];
num_rec=int(sys.argv[6])

recommended = client.send(RecommendItemsToUser(sys.argv[1], 10,return_properties=True, filter= "'generName' == \"{}\" AND 'city' == \"{}\"  AND 'eventDate' >= now()".format(sport,city)));
print(recommended)