import csv
import requests


def get_bone_pairs():
    with (open('pacts/bones.txt')) as f:
        reader = csv.reader(f, delimiter='\t')
        return [line for line in reader]

def get_template():
    with(open('pacts/template.json')) as f:
        return f.read()

def substitute_template(template, consumer, provider):
    return template.replace("__CONSUMER__", consumer) \
    .replace("__PROVIDER__", provider)


def build_url(consumer, provider):
    return "http://127.0.0.1:8090/pacts/provider/" + provider + "/consumer/" + consumer + "/version/1.0.0"

def add_pact(url, pact):
    print("url=" + url)
    requests.put(url, data=pact, headers={"Content-Type": "application/json"})

def process_pact(template, consumer, provider):
    pact = substitute_template(template, consumer, provider)
    url = build_url(consumer, provider)
    add_pact(url, pact)

if __name__ == '__main__':
    template = get_template()
    pairs = get_bone_pairs()
    for pair in pairs:
        process_pact(template, pair[0] + "-bone", pair[1] + "-bone")
