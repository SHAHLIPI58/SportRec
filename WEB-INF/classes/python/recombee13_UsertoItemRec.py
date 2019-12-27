# -*- coding: utf-8 -*-
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
num=int(sys.argv[6])
time=sys.argv[7];

if(sport=="Y"):
	recommended = client.send(RecommendItemsToUser(sys.argv[1], num,return_properties=True,filter="'generName' == context_user[\"favsport\"]  AND 'eventDate' >= now()"))
	print(recommended)

elif(week=="Y"):
	recommended = client.send(RecommendItemsToUser(sys.argv[1], num,return_properties=True, filter=" (now() <= 'eventDate' <= timestamp(\"{}\"))".format(time)))
	print(recommended)

elif(postcode=="Y"):
	recommended = client.send(RecommendItemsToUser(sys.argv[1], num,return_properties=True, filter="'postcode' == context_user[\"postcode\"] AND 'eventDate' >= now() "))
	print(recommended)

elif(city=="Y"):
	recommended = client.send(RecommendItemsToUser(sys.argv[1], num,return_properties=True, filter="'city' == context_user[\"city\"] AND 'eventDate' >= now()"))
	print(recommended)

else:
	recommended = client.send(RecommendItemsToUser(sys.argv[1], 10,return_properties=True, filter= "'generName' == \"{}\" AND 'city' == \"{}\"  AND 'eventDate' >= now()".format(sport,city)));
	print(recommended)






    





