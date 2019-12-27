
from recombee_api_client.api_client import RecombeeClient
from recombee_api_client.api_requests import *
import json
import sys


client = RecombeeClient('illinois-institue-of-tech-dev', 'igCzKWB3UwYFyn6B9NLalbywd9k9TmGOBiLtVZM3e80E0Djsmem55mt0kOYTiNPr')


latitude=sys.argv[2]
longitude=sys.argv[3]
dist=sys.argv[4]
dist=int(dist)*1.60934*1000
recommended = client.send(RecommendItemsToUser(sys.argv[1], 5,return_properties=True, filter="'eventDate' >= now() AND earth_distance(number(\"{}\"),number(\"{}\"),number('stlatitude'),number('stlongitude'))<number(\"{}\") ".format(latitude,longitude,dist)))
print(recommended)
