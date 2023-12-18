import re
from pathlib import Path

regex = re.compile(r"(T0)(.*?)((T1)(.*?)(T3)|(T2)(.*?)(T4))(.*?)((T5)(.*?)(T7)|(T6)(.*?)(T8))(.*?)((T9)(.*?)(T11)|("
                   r"T10)(.*?)(T12))(.*?)(T13)(.*?)(T14)")

p = Path(__file__).with_name('registry.log')
with p.open('r') as file:
    content = file.read()
    new_content = ''
    match_count = 0

    while True:
        new_content = re.sub(regex,
                             r'\2\5\8\10\13\16\18\21\24\26\28',
                             content,
                             1)
        if content == new_content:
            break

        print('===============================================')
        print(f'Content: {content}'.replace('\n', ' '))
        print(f'New Content: {new_content}'.replace('\n', ' '))
        match_count += 1
        print(f'Number of matches: {match_count}')
        content = new_content

    print('===============================================')
    print(f'Finished -> Surplus transitions: {content}')
