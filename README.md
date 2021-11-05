
# RE2DFA
This project is part of a larger project made for the course CS445 Compiler at Imam Mohammad Ibn Saud Islamic University. The goal of this program is to transform regular definition into Deterministic-Finite Automata. Requires Java 11+.
## How to use
1. Download from <a href="">releases</a>.
2. Place it anywhere on your computer.
3. Run it through a console.
<pre>
<code>$ java -jar RE2DFA-{version}.jar "(a|b)*"
Tokens: [a, b, |, *]
NFA graph:
State{0,false}[2]
	:-[null]->State{1,true}
	:-[null]->State{2,false}
State{1,true}[0]
State{2,false}[2]
	:-[null]->State{3,false}
	:-[null]->State{4,false}
State{3,false}[1]
	:-[a]->State{5,false}
State{4,false}[1]
	:-[b]->State{6,false}
State{5,false}[1]
	:-[null]->State{7,false}
State{6,false}[1]
	:-[null]->State{7,false}
State{7,false}[2]
	:-[null]->State{2,false}
	:-[null]->State{1,true}




DFA graph:
0{Accept}:
	:-[a]->1
	:-[b]->2
1{Accept}:
	:-[a]->1
	:-[b]->2
2{Accept}:
	:-[a]->1
	:-[b]->2

NFA constructed with 8 states
Transformed into DFA with 3 states
</code>
</pre>
You can also validate an input:
<pre>
<code>$ java -jar RE2DFA-{version}.jar "(a|b)*" aaaabbbaaba
Tokens: [a, b, |, *]

NFA graph:
State{0,false}[2]
	:-[null]->State{1,true}
	:-[null]->State{2,false}
State{1,true}[0]
State{2,false}[2]
	:-[null]->State{3,false}
	:-[null]->State{4,false}
State{3,false}[1]
	:-[a]->State{5,false}
State{4,false}[1]
	:-[b]->State{6,false}
State{5,false}[1]
	:-[null]->State{7,false}
State{6,false}[1]
	:-[null]->State{7,false}
State{7,false}[2]
	:-[null]->State{2,false}
	:-[null]->State{1,true}




DFA graph:
0{Accept}:
	:-[a]->1
	:-[b]->2
1{Accept}:
	:-[a]->1
	:-[b]->2
2{Accept}:
	:-[a]->1
	:-[b]->2

NFA constructed with 8 states
Transformed into DFA with 3 states
Regex: (a|b)*
Input: aaaabbbaaba is true
</code>
</pre>
## Draw the DFA as graph
<pre>
<code> $ java -jar RE2DFA-{version}.jar "(a|b)*" -v </code>
</pre>
<img src="https://i.imgur.com/ytk6Brc.png" alt="drawing" width="600px"/>

## Special Characters
- \e for epsilon.
- \\. for adding . as a character.
## Planned Features
- Path animation for input.
- Minimizing DFA.
- Support for + and ? quantifiers.
## Current issue
- Overlap characters (see above picture).

