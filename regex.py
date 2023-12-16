import re

regex = re.compile(r"(T0)(.*?)(((T1)(.*?)(T3)(.*?)((T5)(.*?)(T7)(.*?)((T9)(.*?)(T11)|(T10)(.*?)(T12))|(T6)(.*?)(T8)(.*?)((T9)(.*?)(T11)|(T10)(.*?)(T12))))|((T2)(.*?)(T4)(.*?)((T5)(.*?)(T7)(.*?)((T9)(.*?)(T11)|(T10)(.*?)(T12))|(T6)(.*?)(T8)(.*?)((T9)(.*?)(T11)|(T10)(.*?)(T12)))))(.*?)(T13)(.*?)(T14)")

with open("registry.log", "r") as file:
    content = file.read()
    new_content = ""
    count_matches=0
    

    while True:
        # print("Content: ", content)
        new_content, num_substitutions = re.subn(regex, r'\2\6\8\11\13\16\19\22\24\27\30\34\36\39\41\44\47\50\52\55\58\60\62', content)
        print(new_content)
        count_matches+=num_substitutions
        print("Number of matches: ", count_matches)
        if content == new_content:
            break
        else:
            content = new_content
    print("Finished. Surplus transitions: "+new_content)
