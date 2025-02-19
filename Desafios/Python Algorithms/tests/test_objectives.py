import pytest
import json
import random as rand

from project.objectives.priorityqueue import PriorityQueue 
from project.objectives.huffmantree import HuffmanTree
from project.objectives.huffmancoding import HuffmanCoding

objectives = {
    "1": False,
    "2": False,
    "3": False,
    "4": False,
}

MIN_INT = - 500
MAX_INT =   500

def set_objective(id, value):
    global objectives
    if id in objectives.keys():
        objectives[id] = value

@pytest.fixture(scope="module")
def generate_report():
    global objectives
    json_object = json.dumps(objectives, indent=4)
    with open("results.json", "w") as outfile:
        outfile.write(json_object)

class Test_Objective_1:
    """
        Objective 1: Implement PriorityQueue 
    """
    obj = {
        'total': 0,
        'passed': 0,
        }

    pq = PriorityQueue()

    @pytest.fixture(autouse=True)
    def reset_test(self):
        self.pq = PriorityQueue()

    def test_empty(self):
        self.obj["total"] += 1

        assert self.pq.length() == 0
        assert self.pq.isEmpty()
        
        self.obj["passed"] += 1

    def test_push(self):
        self.obj["total"] += 1
        self.pq.push(1)
        assert self.pq.length() == 1
        assert not self.pq.isEmpty()
        self.obj["passed"] += 1

    def test_multiple_push(self):
        self.obj["total"] += 1
        for i in range(15, 0, -1):
            self.pq.push(i)
        assert self.pq.length() == 15
        assert not self.pq.isEmpty() 
        self.obj["passed"] += 1

    def test_pop_empty(self):
        self.obj["total"] += 1
        assert self.pq.isEmpty()
        assert self.pq.pop() == None
        assert self.pq.length() == 0
        self.obj["passed"] += 1

    def test_push_pop(self):
        self.obj["total"] += 1
        assert self.pq.isEmpty()
        for i in range(50,0,-1):
            self.pq.push(i)

        assert not self.pq.isEmpty()
        assert self.pq.length() == 50

        # Check pq is ordered
        for i in range(1, self.pq.length()):
            assert self.pq.pop() == i

        self.obj["passed"] += 1

    def test_compare_tuple(self):
        self.obj["total"] += 1
        self.pq = PriorityQueue(compare=lambda x: x[0] + x[1])
        assert self.pq.isEmpty()
        assert self.pq.length() == 0

        for i in range(15, 0, -1):
            val = (i, i)
            self.pq.push(val)

        for i in range(1,15):
            assert self.pq.pop() == (i,i)

        self.obj["passed"] += 1

    def test_set_objectives(self):
        value = self.obj['total'] == self.obj['passed']
        set_objective( "1", value)

class Test_Objective_2:
    obj = {
        'total': 0,
        'passed': 0,
        }

    huffman = HuffmanTree()

    def test_create_node(self):
        self.obj["total"] += 1

        try:
            _ = self.huffman.createNode(freq=20, left=None, right=None)
            assert 1 == 0 # Should never reached
        except Exception as e:
            assert True

        try:
            _ = self.huffman.createNode(char='A', left=None, right=None)
            assert 1 == 0 # Should never reached
        except Exception as e:
            assert True

        try:
            _ = self.huffman.createNode(left=None, right=None)
            assert 1 == 0 # Should never be reached.
        except Exception as e:
            assert True

        node = self.huffman.createNode(char='A', freq=20, left=None, right=None)
        assert node.char == 'A'
        assert node.freq == 20
        assert node.left == None
        assert node.right == None

        nodeB = self.huffman.createNode(char='B', freq=50, left=None, right=node)
        assert nodeB.char == 'B'
        assert nodeB.freq == 50
        assert nodeB.left == None
        assert nodeB.right == node

        nodeC = self.huffman.createNode(char='C', freq=0, left=node, right=nodeB)
        assert nodeC.char == 'C'
        assert nodeC.freq == 0
        assert nodeC.left == node
        assert nodeC.right == nodeB

        self.obj["passed"] += 1

    def test_compose_node(self):
        self.obj["total"] += 1
        a = self.huffman.createNode(char='C', freq=10, left=None, right=None)
        b = self.huffman.createNode(char='B', freq=25, left=None, right=None)
        
        try:
            ab = self.huffman.composeNode(None,None)
            assert 1 == 0  # Should never be reached
        except:
            assert True

        try:
            ab = self.huffman.composeNode(a,None)
            assert 1 == 0  # Should never be reached
        except:
            assert True

        try:
            ab = self.huffman.composeNode(None,b)
            assert 1 == 0  # Should never be reached
        except:
            assert True
        
        ab = self.huffman.composeNode(a,b)
        assert ab.char == None
        assert ab.freq == a.freq + b.freq
        assert ab.left == a
        assert ab.right == b

        self.obj["passed"] += 1

    def test_frequencies(self):
        self.obj["total"] += 1
        text1 = ""
        text2 = "THIS IS A TEST" # 4 + 1 + 2 + 1 + 1 + 1 + 4 = 14
        text3 = "THIS IS AN INCREDIBLY LONG TEXT THAT YOU SHOULD NOT BE READING BECAUSE IT DOES NOT GIVE ANYTHING BUT A LONG TEXT. I RECOMMEND READING ABOUT HUFFMAN CODING. THERE ARE SOME REALLY GREAT VIDEOS ABOUT IT. CHECK THEM OUT!" # 216

        # The sum of all characters should be the sum of all frequencies.  
        freq_text1 = self.huffman.frequencies(text1) 
        freq_text2 = self.huffman.frequencies(text2) 
        freq_text3 = self.huffman.frequencies(text3) 

        assert freq_text1== {}
        assert sum(freq_text2.values()) == 14
        assert sum(freq_text3.values()) == 216

        # Check all keys(chars) are in the text.
        for k in freq_text2.keys():
            assert k in text2

        for k in freq_text3.keys():
            assert k in text3

        self.obj["passed"] += 1

    def test_huffmantree(self):
        self.obj["total"] += 1

        text2 = "This is a test" # 4 + 1 + 2 + 1 + 1 + 1 + 4 = 14
        text3 = "THIS IS AN INCREDIBLY LONG TEXT THAT YOU SHOULD NOT BE READING BECAUSE IT DOES NOT GIVE ANYTHING BUT A LONG TEXT. I RECOMMEND READING ABOUT HUFFMAN CODING. THERE ARE SOME REALLY GREAT VIDEOS ABOUT IT. CHECK THEM OUT!" # 216

        try:
            self.huffman.createHuffmanTree(1)
            assert 1 == 0  # Should never be reached
        except Exception as e:
            assert True

        try:
            self.huffman.createHuffmanTree("")
            assert 1 == 0  # Should never be reached
        except Exception as e:
            assert True

        self.huffman.createHuffmanTree(text2)
        
        self.obj["passed"] += 1

    def test_set_objectives(self):
        value = self.obj['total'] == self.obj['passed']
        set_objective( "2", value)


class Test_Objective_3:
    obj = {
        'total': 0,
        'passed': 0,
        }

    def test_small_text(self):
        self.obj["total"] += 1
        text = "Hello this is a test code"

        huffman = HuffmanTree()
        huffman.createHuffmanTree(text)

       # The root frequency must be equal to the len of the string passed
        assert huffman.root.freq == len(text)

        codes = huffman.getCodes()
        freq = huffman.frequencies(text)

        assert codes.keys() == freq.keys()
        raw_codes = codes.values()

        assert len(list(raw_codes)) > 0

        # No combination is found. Extremely important if not the compression is wrong
        for c1 in raw_codes:
            for c2 in raw_codes:
                assert c1 + c2 not in raw_codes

        
        self.obj["passed"] += 1

    def test_long_text(self):
        self.obj["total"] += 1

        text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed ac lectus purus. Integer nibh nisl, pretium in nibh ac, mollis interdum dolor. Etiam sit amet fermentum dui, sit amet maximus tellus. Mauris vel commodo nulla. Nulla facilisi. Quisque pulvinar commodo odio et accumsan. Phasellus volutpat luctus quam, fermentum tristique tortor feugiat nec. Morbi non aliquet libero. Nulla sed odio vel erat facilisis congue. Nullam commodo elementum enim, sit amet porta ante pretium at. Quisque sollicitudin lobortis lorem, pulvinar blandit ligula sagittis vitae. Phasellus molestie arcu ipsum, vel dictum felis facilisis eget. Donec convallis varius enim, sed tristique ante rutrum in. Donec porta eros sit amet eros ultrices, a pretium massa rhoncus. Duis a venenatis sapien. Quisque dignissim eu nulla id dapibus. Proin mollis vulputate diam, at dignissim urna facilisis et. Morbi vel egestas metus. Aenean tempus sit amet elit sit amet rhoncus. Vivamus vulputate nisl dignissim augue consequat sollicitudin. Nulla lacus lorem, auctor vel ullamcorper quis, dapibus a tortor. Suspendisse consequat, leo at consectetur accumsan, leo nunc dapibus arcu, at eleifend enim ex eu turpis. Sed condimentum lacus vel dolor accumsan, sed facilisis lorem sollicitudin. Pellentesque vitae aliquam libero, ac imperdiet justo. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Phasellus porta, odio nec gravida dignissim, est purus blandit ex, nec ullamcorper dolor justo ut tellus. Cras vel mollis elit. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Sed vel tellus facilisis, suscipit libero sit amet, ornare ligula. Suspendisse placerat maximus facilisis. Quisque tristique tempor rutrum. Nullam laoreet leo sit amet vulputate cursus. Quisque pharetra, sapien vel tincidunt porta, elit magna condimentum velit, sed rutrum enim sapien vel ligula. Proin sollicitudin, nibh hendrerit fermentum dignissim, augue elit viverra nulla, sed semper lectus metus a ligula. Vestibulum consequat, urna ac placerat hendrerit, leo justo aliquam libero, ut feugiat erat mi at nunc. Quisque commodo, elit eget fringilla commodo, lectus arcu consectetur ipsum, id vestibulum justo libero eu nunc. Donec laoreet convallis vulputate. Nunc placerat libero quis venenatis laoreet. Donec at congue magna. Etiam vel nibh vel odio ultrices tincidunt. Vestibulum porttitor tincidunt nisl interdum venenatis. Duis facilisis neque ac placerat pulvinar. Praesent eget quam enim. Proin euismod lorem a fermentum fermentum. Donec arcu lorem, hendrerit vitae luctus quis, feugiat iaculis nisi. Sed id fermentum metus, sit amet tincidunt quam. Donec facilisis, massa quis fermentum ullamcorper, lorem turpis commodo diam, non tempus risus dolor sed eros. Suspendisse eget sollicitudin dui, ac consectetur felis. Sed elit turpis, ornare at cursus in, blandit eget nulla. Nulla facilisi. Ut et lacus nisi. Maecenas ut lectus porttitor, semper massa at, tincidunt urna. Curabitur eleifend dictum nisl, non interdum dolor molestie et. Aenean sed aliquet justo. Duis facilisis interdum lectus eu dapibus. Maecenas at iaculis tortor. Vestibulum venenatis ac quam sed pulvinar. Nunc sagittis mi orci, id tempus elit convallis a. Donec odio est, rutrum vel orci non, consectetur rhoncus sapien. Aenean gravida arcu vitae pharetra aliquet. Duis at euismod tortor, eu rutrum tellus. Sed ut massa sit amet orci lobortis pharetra vel nec nisl. Donec condimentum commodo ligula, a rutrum diam lobortis ut. Maecenas et aliquam tortor, sit amet molestie enim. Quisque et laoreet ligula. Curabitur congue magna vitae risus accumsan porttitor. Duis vitae nisi at ipsum mollis dictum. Phasellus tristique velit sed mauris porta ullamcorper. Nam nec mauris id turpis sodales euismod. Donec magna enim, tristique ut sagittis a, sollicitudin ut sem. Donec posuere, orci quis mattis scelerisque, ligula ligula sollicitudin metus, quis iaculis turpis turpis eu diam. Morbi vitae nulla vel ligula pellentesque molestie sed a lorem. Suspendisse orci dui, rhoncus sed tincidunt vel, facilisis eu urna. Pellentesque semper placerat metus at mattis. Aliquam tempor dui lorem, sit amet molestie diam eleifend quis. Ut libero odio, gravida in feugiat ullamcorper, venenatis non justo. Quisque et nulla mattis, luctus sapien eu, pellentesque nunc. Sed ultrices commodo nibh ut mollis. Nunc non mi lacinia, sodales dolor nec, lacinia velit. Mauris scelerisque varius sagittis. Donec consectetur elit non odio consectetur luctus. Praesent ut condimentum orci, suscipit congue enim. Vestibulum sollicitudin sem nec lectus varius auctor. Pellentesque dapibus vehicula ante eu suscipit. Donec quis convallis sapien. Suspendisse eget tortor bibendum, fermentum ipsum vitae, viverra magna. Donec rhoncus, enim sit amet imperdiet fermentum, urna nunc bibendum turpis, id bibendum est nibh in eros. Pellentesque ac consequat nisl. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; In facilisis lectus sit amet mauris hendrerit, vel vulputate est dictum. Duis a nisi at mi tempus scelerisque. Nullam magna justo, consequat ut efficitur at, ornare in diam. Quisque vehicula orci id arcu laoreet mattis. Suspendisse eget dignissim augue, sed egestas ante. Phasellus consectetur cursus massa. Donec vehicula enim ac ex viverra molestie. Vivamus pulvinar nunc et turpis ultricies, at ornare enim malesuada. Integer ac malesuada felis, id dapibus ex. Proin blandit, justo in efficitur molestie, ipsum velit pharetra nunc, eu ullamcorper dui velit eu nisl. Duis quis venenatis est, euismod fringilla purus. Morbi sed odio ex. Vivamus eu erat risus. Nullam vel elementum orci, sit amet faucibus dui. Pellentesque at vulputate nisi, eu condimentum eros. Proin commodo nulla nec felis auctor, et sodales ipsum consequat. Nulla eget pellentesque quam. Mauris lobortis est vitae nibh malesuada porttitor. Vestibulum malesuada augue eu nulla tempor, et scelerisque lectus blandit. Morbi elementum suscipit semper. Cras mauris magna, venenatis ac ante ac, efficitur ultrices lectus. Nunc placerat odio velit, sit amet accumsan eros molestie a. Nunc efficitur quam ut elementum rhoncus. Praesent commodo dui vel leo rutrum posuere."

        huffman = HuffmanTree()
        huffman.createHuffmanTree(text)

       # The root frequency must be equal to the len of the string passed
        assert huffman.root.freq == len(text) 

        codes = huffman.getCodes()
        freq = huffman.frequencies(text)

        assert codes.keys() == freq.keys()
        raw_codes = codes.values()

        assert len(list(raw_codes)) > 0

        # No combination is found. Extremely important if not the compression is wrong
        for c1 in raw_codes:
            for c2 in raw_codes:
                assert c1 + c2 not in raw_codes

        
        self.obj["passed"] += 1

    def test_set_objectives(self):
        value = self.obj['total'] == self.obj['passed']
        set_objective( "3", value)


class Test_Objective_4:

    obj = {
        'total': 0,
        'passed': 0,
        }

    def test_encode(self):
        self.obj["total"] += 1

        text1 = "Hello"
        text2 = "Hello this is another test text"
        text3 = "A text is a passage of words that conveys a set of meanings to the person who is reading it. It’s a body of written work, in various forms and structures, that can be words, phrases and sentences that piece together a passage of written work."
        text4 = "Type parameters declared through a type parameter list are visible within the scope of the declaration and any nested scopes, but not in the outer scope. For example, they can be used in the type annotations for the methods of a generic class or in the class body. However, they cannot be used in the module scope after the class is defined. See Type parameter lists for a detailed description of the runtime semantics of type parameters."

        bint1, hufft1 = HuffmanCoding.encode(text1)
        assert len(bint1) < (len(text1) * 8) * 0.60
        assert len(bint1) > 0

        bint2, hufft2 = HuffmanCoding.encode(text2)
        assert len(bint2) < (len(text2) * 8) * 0.60
        assert len(bint2) > 0

        bint3, hufft3 = HuffmanCoding.encode(text3)
        assert len(bint3) < (len(text3) * 8) * 0.60
        assert len(bint3) > 0

        bint4, hufft4 = HuffmanCoding.encode(text4)
        assert len(bint4) < (len(text4) * 8) * 0.60
        assert len(bint4) > 0

        self.obj["passed"] += 1

    def test_decode(self):
        self.obj["total"] += 1
        text = "Type parameters declared through a type parameter list are visible within the scope of the declaration and any nested scopes, but not in the outer scope. For example, they can be used in the type annotations for the methods of a generic class or in the class body. However, they cannot be used in the module scope after the class is defined. See Type parameter lists for a detailed description of the runtime semantics of type parameters."

        bint, hufft = HuffmanCoding.encode(text)
        assert len(bint) < (len(text) * 8)
        assert len(bint) > 0 

        decodedText = HuffmanCoding.decode(bint, hufft)
        assert decodedText == text 

        self.obj["passed"] += 1

    def test_set_objectives(self):
        value = self.obj['total'] == self.obj['passed']
        set_objective( "4", value)


def test_generate_report(generate_report):
    assert 1 == 1
