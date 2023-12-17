import re
from pathlib import Path

regex = re.compile(r"(T0)(.*?)(((T1)(.*?)(T3)(.*?)((T5)(.*?)(T7)(.*?)((T9)(.*?)(T11)|(T10)(.*?)(T12))|(T6)(.*?)(T8)("
                   r".*?)((T9)(.*?)(T11)|(T10)(.*?)(T12))))|((T2)(.*?)(T4)(.*?)((T5)(.*?)(T7)(.*?)((T9)(.*?)(T11)|("
                   r"T10)(.*?)(T12))|(T6)(.*?)(T8)(.*?)((T9)(.*?)(T11)|(T10)(.*?)(T12)))))(.*?)(T13)(.*?)(T14)")

p = Path(__file__).with_name('registry.log')
with p.open('r') as file:
    content = file.read()
    match_count = 0

    while True:
        print('===============================================')
        print(f'Content: {content}'.replace('\n', ' '))

        content, substitutions = re.subn(regex,
                                         r'\2\6\8\11\13\16\19\22\24\27\30\34\36\39\41\44\47\50\52\55\58\60\62',
                                         content)
        print(f'New Content: {content}'.replace('\n', ' '))

        match_count += substitutions
        print(f'Number of matches: {match_count}')

        if substitutions == 0:
            break

    print('===============================================')
    print(f'Finished -> Surplus transitions: {content}')
