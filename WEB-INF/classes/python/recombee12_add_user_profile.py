
from recombee_api_client.api_client import RecombeeClient
from recombee_api_client.api_requests import *
import json
import sys

print(sys.argv[1])


client = RecombeeClient('illinois-institue-of-tech-dev', 'igCzKWB3UwYFyn6B9NLalbywd9k9TmGOBiLtVZM3e80E0Djsmem55mt0kOYTiNPr')

#client = RecombeeClient('iitchicago-test4', 'SYaARkmc7KWE6kjZkESn3xRKozUQVFkhfdDPcUfHSvzWK75GDPdYULPMYFAl8qc1')


client.send(SetUserValues(sys.argv[1], {"city":sys.argv[2],"favsport":sys.argv[3],"postcode":sys.argv[4],"stlatitude":sys.argv[5],"stlongitude":sys.argv[6]}, 
                          cascade_create= True)
            );
    





