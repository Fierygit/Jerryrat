# <center>easy_c</center>

## 实现目标

// todo 完善代码和博客

实现一门基于 x86 平台的编译器， 链接基于GNU as， 功能实现加减乘除， 函数调用， if 语句和while循环，，输出整数值和字符串，不实现指针和数组。

目的在于实现编译器的功能， 所以没有做很强大的语法功能， 自选了几个常用的作为实现， 虽然弱小，但五脏俱全！

湖南大学编译原理实验1-8， 这里大概介绍实现的思路， 具体太多细节， 看原码： [github](https://github.com/Fierygit/easy_c)

开始实现时参考了 青木的自制编译器， 发现书中用到的语言是java，我打算使用c来实现， 发现有很多的不同， 书中前段使用了java的正则表达式等， 我想使用编译原理课上学过的知识来实现。





## 语法定义

这一部分在开始的没有列全，好后悔好后悔！！！倒置项目在进行到最后的时候， 发现这也忘记考虑了， 那也忘记考虑了， 吸取经验： **在写项目的时候， 一定先要有一个实现目标**。 不然到最后突然发现， 输出字符串词法分析没有实现， 倒置到最后只能强前面几千方的代码， 也只能使用ID作为字符串输出ID的值。-_-



#### 运算符：

```c
+ - * /
```

#### 逻辑运算：

```c
== 	<= 	>=
```

#### 操作符：

```c
+= 	 -=	 /= 	*=
```

#### 变量：

变量使用前统一声明， 在函数开始时， 或者全局声明

```c
int a;
int max(){
	int a;
	a = 10;
    return a;
}
//函数里面的a会首先找函数体里面声明的变量， 然后再找全局变量里声明的符号
```

所有的大括号不能省略！

```c
if(a == 10) return a;  // error!!!
while(true);           //error!!!
```

#### 输出：

```
int a;
a = 10;
print(a);
// > 10
print[a];
// > a
```







-----

## 词法分析

#### 关键字

 **else  if  int  return  void  while** **print**

#### 专用符号

```
+ - * / <  > = ; , ( )  { } /* */
```

<!-- more -->

#### 其他标记

ID = letter letter*

NUM = digit digit*

#### 逻辑运算

```
== <= >=
```

#### 操作数

```
+= 	-=	  *=	 /=
```

#### 实现方法：

使用状态机：

![](https://raw.githubusercontent.com/Fierygit/picbed/master/20200118150829.png)

![](https://raw.githubusercontent.com/Fierygit/picbed/master/tes)

其它类似方法！！！

每次读入一个字符就判断它去了那个状态， 简单的编译器使用这种方法是实现简单，但是当编译器大的时候， 这种情况就变的十分的庞大！



#### 数据结构：

```c
struct Token{
  string type;
  string value;
  int row;			// 保留每个token的 行数
  int column;		// 保留每个token的  列数
  Token(string type, string value, int row, int column){
      this->type = type;
      this->value = value;
      this->row = row;
      this->column = column;
  }
};
```

保存每一个记号的 行数 和 列数

![image-20200118195516438](C:%5CUsers%5CFirefly%5CAppData%5CRoaming%5CTypora%5Ctypora-user-images%5Cimage-20200118195516438.png)

上面图片是将每一个token存入 vector 中， 最后将其输出！





---

## 语法分析：

语法分析采用的是 LL（1） 的递归下降文法！ 向前看看一个字符， 首先转换为右递归文法！！

LL（1）是。。。。。。

##### NFA文法：（提取自c-编译器）

```
param ->  INT
param_list  ->  param  |  param_list, param
params -> NULL |  param_list
local_declaration  ->   var-decaration  | NULL
arg-list → arg-list , expression|expression
args → arg - list | empty
call →  ID (args)
factor  ->  ( expression ) | var | call | NUM
term   -> factor  op  term  | factor   右循环
additive_expression   ->    term  |   term op  additive_expression ;  op = + | -
simple_expression  ->  additive_expression 
				|  additive_expression op additive_expression  (op == >= <=)
iteration_stmt    ->   while(expression) { statement_list }
expression    ->   simple_expression  |  var = expression
expression_stmt   ->   expression ; || ;
statement ->  iteration_stmt | selection_stmt | expression_stmt |
				return_stmt | print_stmt
statement_list   ->   statement_list statement || NULL
compound_stmt ->  local_declaration & statement_list
declaration  -> var-declare | fun-declare
declaration_list   ->  declaration |  declaration_list
。。。。。
参考parse.cpp
```

由此生成语法树！

```c
struct TreeNode {
  struct TreeNode *child[4];  //四个子节点
  struct TreeNode *sibling;   //存储兄弟节点
  int tokenIndex;             //存储代码的位置, 可以获取到信息
  NodeKind nodekind;          //存储类型
  string value;

  bool isVisit;  // 中间代码生成的时候用
};
```

一个函数最多有四部分构成  int max （ ） ｛  ｝

所以使用 四个子孩子就行了！



---

## 语义分析

语义分析主要有两大部分：



#### 一、变量声明

当有一个变量声明， 把声明加入符号表， 之后要是用到了这个变量就查找符号表！

```c
struct VariableInfo {
  string name;
  int lineNo;      //所在的代码行数
  int location;    // 在内存的位置
  string type;     //变量的类型， 只有一个类型 INT
  TreeNode *node;  //记录声明的节点在哪里
};

// 记录 每一个变量声明的地方  < 函数名字，  < 变量名字， Variableinfo  >
extern map<string, vector<VariableInfo> > var_table
```

// 记录 每一个变量声明的地方  < 函数名字，  < 变量名字， Variableinfo  >

通过函数 变量名字找信息， 全局变量的 函数名字为 golbal， 在找一个id是否有定义的时候， 通过这个map找信息，  c语言是可以重复命名的， if 里面可以定义变量， 在一开始定义语言的时候， 想的是使用变量时必须函数一开始要定义， 没想到要在if里面也可以定义， 所以这里这个数据结构够用了， 如果要考虑作用域， 加多一个 scope 的变量， 标记同一个变量名的作用域， 作用域从小往大找， 找到global都没有就报错！



#### 二、类型检查

类型检查包括两大部分：

##### 函数参数检查

每当有一个函数声明， 记录函数的参数个数， 当调用的时候，检查类型和参数！

```c
typedef vector<string> vstr;
struct FunctionInfo {
  int param_num;       // 参数的个数
  vstr param_type;     // 每个参数的类型
  string return_type;  // 返回的类型
  TreeNode *node;      //记录声明的节点在哪里
};

// 记录每一个 函数的信息符号表, 全局变量也在这里
extern map<string, FunctionInfo> fun_table;
```

这个函数的作用是记录， 函数的信息， 每当有一个函数声明的时候加入这个数据结构， 当有函数调用的时候， 找这个表，检查参数的个数！



##### 语句类型检查

检查语句两边的参数是否正确， 不正确的话，输出错误！

如果有一个语法树的节点为 + ， **递归**检查这个节点的两边是否都为整形！



---

## 中间代码生成

前序遍历语法树， 把对应的节点生成 **三地址码**。

这部分最主要的是设计数据结构， 由于一开始没有设计好数据结构， 发现后面写的很乱！！！！！

提前想好程序是怎么跑的很重要！！

```c++
struct MidArgs {
  // type 分为 STR, ID， TMP,  NUM；
  // bool  用INT  0  1 表示
  string type;
  string value;
  // 代码有全局变量和非全局变量
  // 到符号表里面找， 全变量用伪标签
  // 堆： 自己创建的内存（不用）   栈： 函数临时变量   全局区：
  // 静态和全局变量区（伪标签实现） 字符串 属于全局区
  bool isGlobal;
  int offset;  //栈中, type = tmp, id                  全局变量直接用！！！，
               //不用找
};
// 函数调用     call(10, a);
// op = param arg1 = a  arg2 = NULL   op = param  arg1 = 10  arg2 = NULL
// op = call  arg1 = id       op  = return arg1 = id or null
struct MidCodeItem {
  string dest;
  string op;
  MidArgs *arg1;
  MidArgs *arg2;
};

struct MidCode {
  string funcName;
  int stackSize;  // 只有整形的值， 只考虑4字节， 因此只需要偏移量, 动态变化
  vector<MidCodeItem *> item;
};

extern vector<MidCode *> midCode;
// 声明 ID 加入， value随机初始     TMP 生成一条语句加入 ， 或更改id，
extern map<pair<string, string>, MidArgs *> stackInfo;  // ID, TMP, 加入

```

这一部分我写的最头疼一部分的时候， 太多变量要去考虑， 刚开始的时候每出现一个变量就去看看他的定义， 没办法， 每一个变量都要用到。



每一个操作数的节点，+ - * / 都会生成一个临时变量， 函数调用也会。用一个全局变量递增临时变量， 加上双下划线作为前缀防止变量名冲突！！！

（注意函数调用的三地址码）

下面是其中一个代码的中间代码： 

dest 											op												arg1													arg2

```c
FUNCTION: VAR_DECLARE	(stackSize: 0)
	                    INT                 	[p, ID, 0, 1]                 NONE                          
FUNCTION: test	(stackSize: 4)
	__t1                +                   	[a, ARG, 0, 0]                [b, ARG, 1, 0]                
	ans                 =                   	[__t1, TMP, 1, 0]             NONE                          
	__t2                -                   	[ans, ID, 0, 0]               [1, NUM, 0, 0]                
	                    PRINT               	[__t2, TMP, 2, 0]             NONE                          
	__t3                -                   	[a, ARG, 0, 0]                [1, NUM, 0, 0]                
	                    return              	[__t3, TMP, 3, 0]             NONE                          
FUNCTION: main	(stackSize: 9)
	a                   =                   	[3, NUM, 0, 0]                NONE                          
	p                   =                   	[1, NUM, 0, 0]                NONE                          
	Label0:             LABEL               	NONE                          NONE                          
	                    if                  	[a, ID, 0, 0]                 NONE                          
	                    goto                	[Label1:, LABEL, 1, 0]        NONE                          
	                    goto                	[Label2:, LABEL, 2, 0]        NONE                          
	Label1:             LABEL               	NONE                          NONE                          
	__t3                =                   	[a, ID, 0, 0]                 NONE                          
	__t4                =                   	[p, ID, 0, 1]                 NONE                          
	                    ARG                 	[__t4, TMP, 4, 0]             NONE                          
	                    ARG                 	[__t3, TMP, 3, 0]             NONE                          
	__t5                CALL                	[test, CALL, 0, 0]            [8, ARG_NUM, 0, 0]            
	a                   =                   	[__t5, TMP, 5, 0]             NONE                          
	                    goto                	[Label0:, TMP, 6, 0]          NONE                          
	Label2:             LABEL               	NONE                          NONE                          
	                    PRINT               	[4, STR, 0, 1]                NONE                          
	                    PRINT               	[space, STR, 0, 1]            NONE                          
	                    PRINT               	[5, STR, 0, 1]                NONE                          
	                    PRINT               	[endl, STR, 0, 1]             NONE                          
	                    if                  	[1, NUM, 0, 0]                NONE                          
	                    goto                	[Label3:, LABEL, 7, 0]        NONE                          
	                    goto                	[Label4:, LABEL, 8, 0]        NONE                          
	Label3:             LABEL               	NONE                          NONE                          
	p                   =                   	[2020, NUM, 0, 0]             NONE                          
	                    PRINT               	[p, ID, 0, 1]                 NONE                          
	Label4:             LABEL               	NONE                          NONE                          
	                    return              	[0, NUM, 0, 0]                NONE     
```

总结这一步分， 烦， 烦人！ 想吐





到这一部分和前面感觉又分开了！！！^ _ ^，  我只需要遍历中间代码的数据结构， 生成对应的汇编， 首先还得学汇编，，， 于是另一篇博客产生， 总结汇编用到的指令

## 汇编代码生成

总思路：

把字符串放到 rodata静态变量段

全局变量通过 comm 放到 静态数据段

所有的变量直接进行压栈处理

函数一开始，先分配所有变量用到的空间大小

当要函数调用时， 有多少个参数就扩大多少栈空间，在函数调用后返回值在eax， 再还原栈空间

还有很多对应汇编知识和实现方式细节：

[blog]()



## 后端优化

#### 优化一：活性分析

按照之前的思路， 在生成汇编代码的时候， 有很多临时变量也压栈了， 但是大多数的临时变量其实只用了一次， 也就是说一次性用品， 按照上课的思路， 来一个活性分析， 当一个变量在后面没有再被引用的情况， 把这个变量在栈的空间赋给另一个变量使用！！！ 但是这个实现有点难， 老师也只讲了思路，有点复杂， 没有实现



#### 优化二：常量折叠

当一个语法树节点为+ - 等， 而且左右子节点树都为常量， 提前计算好！！！这个遍历语法书就行了



#### 优化三：提前条件判断

if(0)  这种情况直接不要！！！！











