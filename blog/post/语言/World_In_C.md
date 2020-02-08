---
title: Wolrd In C
date: 2019-11-2
author: Firefly
img:
top: true
cover: false
coverImg: 
toc: true
mathjax: false
categories: C/C++
tags:
  - c/c++
---





## 位运算

~ 用于数字或者整形变量之前，表示对该数取反操作，其规则是~0=1, ~1=0, 如二进制0101 0101取反后就是1010 1010

测试得到： -a - 1  ==  ~a 

判断 2 的 乘方：

```c
n & n - 1
```

输出二进制 1 的个数:

```
int cnt = 0;
while(n) n &= n-1, ++cnt
```







## 格式化标准输入输出

cout.setf(ios::left);   //左对齐 右对齐相同 

 cout.width(3);    //宽度为3



## struct 初始化的一个小细节

2019-11-2 
1.今天写实验定义了一个这样的结构体，然后赋值： 

![](https://raw.githubusercontent.com/Fierygit/picbed/master/c1.png)


![](https://raw.githubusercontent.com/Fierygit/picbed/master/c2.jpg)


debug了半天， 一个一个输出，终于找到错误的源头，原来是。。。。。。string和struct有冲突，什么原因呢？

经查，  malloc/free和new/delete 

---

- new会先调用operator new函数，申请足够的内存（通常底层使用malloc实现）。然后调用类型的构造函数，初始化成员变量，最后返回自定义类型指针。delete先调用析构函数，然后调用operator delete函数释放内存（通常底层使用free实现）。

---

- malloc/free是库函数，只能动态的申请和释放内存，无法强制要求其做自定义类型对象构造和析构工作。

---

所以解决的方案是： ```TreeNode *temp = new TreeNode;```

struct 是c里面的， 而string 类是 c++的，所以当在用c++的时候，写c语言就要考虑一下兼容问题。



## map的一个小细节

当创建一个map的时候，map是没有预分配内存的，当map一个没有的key的时候，就会默认创建这个对象，并且map的size加一！

看一个例子：

```cpp
#include <iostream>
#include <map>
using namespace std;
int main(){
    map<string,int> map1;
    map<string,string> map2;
    cout << "out1-----------\n";
    cout << "map1.size(): " <<  map1.size() << "\t" << endl  ; 
    cout << "map2.size(): " <<  map2.size() << "\t"  ;
    cout << "\n out2-----------\n";
    cout <<"map1[a]: " <<  map1["a"] << endl;
    cout <<"map2[a]: " <<  map2["a"] << endl;
    if(map2["a"] == "") cout << "empty string!!!!" << endl;
    cout << "map1.size(): " <<  map1.size() << "\t"  << endl ;
    cout << "map2.size(): " <<  map2.size() << "\t"  << endl ;
    return 0;
}
```

```cpp
out1-----------
map1.size(): 0
map2.size(): 0
 out2-----------
map1[a]: 0
map2[a]:
empty string!!!!
map1.size(): 1
map2.size(): 1
```

当写一些需要用到map的代码的时候，就要注意了， 可能要花掉你半天的时间去debug这个小问题！！！

## 操作系统的 堆和栈

学操作系统的时候，系统分配内存会分配 堆和栈

有一个我纠结了很久的问题，堆是一个二叉树实现的，而分配内存的堆是一个线性的地址，这两者有什么关联呢？

结论，数据结构没有学好！！  找个时间马一个heap

栈和堆都可以用二叉树实现， 堆栈就是栈， 堆插入一个元素二叉树放到最后面，如果删除了父节点，就要调整二叉树， 而栈只能在二叉树尾部删除和操作，没有其它操作，所以堆栈的含义：堆的后面多了一个限定词， 把范围限定的更小了！！ 但栈一般直接使用数组就好了，查看网上的说法：栈是一种受限制的堆。

查看数据结构的书本，发现这样一句话，我还画了出来 ： 学生们经常把堆的逻辑表示与利用基于数组的完全二叉树的物理实现相混淆。二者并非同义，虽然堆的一般实现方法是使用数组，但是从逻辑的角度来看，堆实际上是一种树结构。

```cpp
int a = 0; 全局初始化区
char *p1; 全局未初始化区
main()
{
int b; 栈
char s[] = "abc"; 栈
char *p2; 栈
char *p3 = "123456"; 123456\0在常量区，p3在栈上。
static int c =0； 全局（静态）初始化区
p1 = (char *)malloc(10);
p2 = (char *)malloc(20);
分配得来得10和20字节的区域就在堆区。
strcpy(p1, "123456"); 123456\0放在常量区，编译器可能会将它与p3所指向的
"123456"优化成一个
```

```cpp
#include <stdio.h>
#include <stdlib.h>
int main() {
  int a = 1;
  int b = 2;
  int c = 3;
  int d = 4;
  int e = 5;
  printf("addr: %p\n", &a);
  printf("addr: %p\n", &b);
  printf("addr: %p\n", &c);
  printf("addr: %p\n", &d);
  printf("addr: %p\n", &e);
  int *a1, *b1, *c1, *d1, *e1;
	printf("\n-----------------\n");

  a1 = malloc(sizeof(int));
  b1 = malloc(sizeof(int));
  c1 = malloc(sizeof(int));
  d1 = malloc(sizeof(int));
  e1 = malloc(sizeof(int));

  printf("addr: %p\n", &a1);
  printf("addr: %p\n", &b1);
  printf("addr: %p\n", &c1);
  printf("addr: %p\n", &d1);
  printf("addr: %p\n", &e1);
  return 0;
}
```

```cpp
输出
addr: 0061FF1C
addr: 0061FF18
addr: 0061FF14
addr: 0061FF10
addr: 0061FF0C
-----------------
addr: 0061FF08
addr: 0061FF04
addr: 0061FF00
addr: 0061FEFC
addr: 0061FEF8
可以看出  heap 是 从减小的，  stack是增大的
```



for(int i = 0, siz = a.size(); i < siz; i++)  //for循环可以有有四个值

​      有时也可以两个值       for( ; a < b; ++a); 

return cout << " " << endl,0;      //可以在return 后面输出

memset(G, 0x3f, sizeof(G));   //  输出这个1061109567,给数据赋值无穷大的方法（原理。。。）

在for循环里 ++i 比 i++ 的效率高 [详情请点击](https://blog.csdn.net/FireflyNo1/article/details/82809624)