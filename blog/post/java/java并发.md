## <center> java 并发与实战</center>



## 基础知识



### 线程的安全性



**安全的线程满足条件**：

- 不在线程之间共享变量
- 将变量置为不可变变量
- 访问变量时使用同步



**原子性：**

```java
value = value + 1；
```

 在 cpu  执行  最终的机器指令时， 分为两步， 首先获取value， 然后再加上一返回， 这两步一起执行叫做原子性。



**竞态条件：**

两个线程同时获取了， value， 同时加上了1， 导致，value 只加上了1，这种情况称为竟态条件。



**延时初始化的竟态条件：**

```java
public class Test{
    private Test test = null;
    public Test getInstance(){
        if(test == null)
            test = new Test();
        return test;
    }
}
```

当两个线程调用这个获取对象的方法时， 一开始所有的 对象获取的都是null， 于是test被初始化了两次！ 存在竟态条件， 如果是单例模式， 那么返回的将不是单例！



**复合操作：**

相当于操作系统的硬件提供的支持， 让一个方法为原子执行， 如 TestAndSet 的函数等等！将数据的操作复合到一起， 保证原子性.

```java
AtomicInteger count = new AtomicInteger();
```

当使用上面的整数时，默认是原子操作的，由jvm虚拟机提供的方法！

操作系统中学的是硬件提供支持， 而 java虚拟机也可以提供支持！



**内置锁：**（也称监视器锁 或 监视器）

```java
synchronized(class){
    // 自动获取锁， 保证代码的原子性执行， 但是这样做， 性能低下！！！ 把并行的代码，变成了串行
} 
```

静态的synchronized 方法以Class 对象作为锁！



**重入：**

一个线程可以重复获取锁， JVM 会记录线程获取锁的次数，直到所有的锁（重复获取的）都释放了才释放锁。

 

**多状态的保护：**

当有多个状态的时候， 要有一个锁来保护所有的状态！！



### **对象的共享**

  

**可见性：**

一个对象的发生了变化， 其它线程要能够第一时间得知， 变量发生了变化！

锁能够保证变量的可见性！



**Volatile** 

将变量声明为volatile， 确保变量在更新操作时通知其他线程。**只能确保可见性**

通常用作 某个操作完成，发生中断 或者 状态的标志。

使用时需要满足下面的所有条件：

- 变量的写入操作不依赖当前值， 或者确保只有单个线程更新变量的值
- 变量不会与其它状态变量一起纳入不变性条件中
- 访问变量时不需要加锁



**发布与逸出**

> 发布： 使对象能够在当前作用域之外的代码中使用(包括引用)

> 溢出： 某一个不该发布的对象被发布时， 称为逸出

当从对象的构造函数中发布对象时， 只是发布了一个尚未构造完成的对象！！！



**线程封闭：**

单线程内访问数据， 不需要同步， 称为线程封闭！

如JDBC使用的connection对象， 分发到一个线程中， 用完后还回来， 隐含使用了线程封闭



**栈封闭：**

栈封闭是线程封闭的一种特例，在栈封闭中，只能通过局部变量才能访问对象。（没什么用，其它线程访问不到）



**ThreadLocal类：**

这个类可以使线程中的某一个值与保存的值的对象关联起来， 每一个线程对应都有一个副本





**不变性：**

不变的对象一定是线程安全的！

满足条件有：

- 对象创建之后就不可以更改
- 对象所有的域都是final 类型的
- 对象是正确构造的（在对象的创建期间， this 引用没有逸出）



使用 volatile 发布不可变对象可以保证线程安全

```java
public class Test{
    private final Test1 test1;
    private final Integer mapTest1;
    
    public Test(Integer i, Test1 t){
        this.test1 = t;
        this.mapTest1 = i;
    }
    public int int2Test(Integer i){
        return  mapTest1 == i? test1 : NULL;
    }
}
```

上面所有域都是不可变的， 创建之后不可修改， 构造的时候没有溢出， 因此是一个不可变类

因此是线程安全的！



**安全发布**



不可变对象可以被安全地发布， java内存模式提供初始化安全的保证。

（final 指向的域是可变的， 那么指向的域也需要同步）



**安全发布的常用模式**：

- 在静态初始化函数中初始化一个对象的引用（JVM 存在同步机制，在初始话阶段执行！一定安全）
- 将对象的引用保存在volatile 类型的域 或者 AtomicReferance 对象中
- 将对象的引用保存到某个正确构造对象的final 类型域中
- 将对象的引用保存到由一个锁保护的域中



**事实不可变对象**必须通过安全的方式发布

```java
public Map<String, Date> name2date =
    Collections.synchronizedMap(new HashMa<String, Date());
```





### 对象的组合



**设计线程安全的类：**

- 找出构成对象状态的所有变量
- 找出约束状态变量的不变性条件
- 建立对象状态的并发访问管理策略



**实例封装：**

将数据封装在对象的内部， 将数据的访问限制在对象的方法上， 从而确保线程安全



**java监视器模式：**

对变量的封装通过 方法来执行， 而这些方法都是同步的！

```java
class Counter{
    private long value;
    public synchronzed long getValue(){
        return value;
    }
    public synchronzed void setValue(long value){
        this.value = value;
    }
}
```



**使用私有锁来保护变量：**

```java
class PrivateLock{
    private final Object myLock = new Object();
    void method(){
        synchronzed(myLock){
            // 访问 保护的变量
        }
    }
}
```

相比监视器模式， 程序的维护较难， 检查是否正确时要检查整个程序而不是单个类（其它类可以通过方法获取lock  ）。



以上都是一个类的情况

----

如果有多个类，**基于委托**， 将线程的安全委托给别的类来处理，如将数据放入concurrentHashMap中。

多个线程安全的类组合在一起，不一定线程安全， 要视情况而定：

- 当多个变量之间有约束条件时， 组合在一起就不线程安全了！





### 基础构建模块



#### 同步容器类

包括 Vector 和 Hashtable， 这些整个类是线程安全的，符合操作也是线程安全的！

**存在问题**

getValue 和 removeValue 两个方法的复合操作都是线程安全，但是组合在一起时，如果并发执行， 有可能一个线程先remov最后一个元素， 而继续getValue！

还有一种情况在迭代的时候产生， 一个线程迭代元素，另一个线程删除了某一个元素， 会报错！

```java
for(int i = 0; i < vector.size(); i++){
    vector.get(i);
    //当i 进入到了最后一个元素，但还没有get， 此时跳到另一个线程，删了它，再回到这里 get一个没有的元素！
}
```

toString 也是一个隐藏的迭代器！



如果要安全地迭代同步容器类， 可以在客户端给整个容器加锁， 加锁之后，其它操作访问不到， 但这样效率低下，当容器很大的时候！

引出：

#### 并发容器类

##### **concurentHashMap**

对于HashMap，如果散列函数设计的不好，都散列到了同一条的散列表上，当遍历这条散列表时，其它的线程不能访问这条散列表， 导致效率低下！

 concurrentHashMap 没有实现对map的加锁访问，不是在每一个方法上同步， 而是使用分段锁！（详见分段锁）



##### CopyOnWhiteArrayList

顾名思义， 在写的时候会复制一遍，迭代线程和修改线程不会相互影响！



#### 同步工具类

##### **CountDownLatch** 

闭锁可以让所有的线程都到达时执行！

初始化为一个正数， 代表等待的线程， 当没有等待线程的时候继续运行！

```java
public class Latch {
    
    public void calc() throws Exception {
        final CountDownLatch startLatch = new CountDownLatch(1);
        final CountDownLatch endLatch = new CountDownLatch(10);

        for (int i = 0; i < 10; i++) {
            new Thread(()  -> {
                try{
                    startLatch.await();
                    System.out.println(Thread.currentThread().getName() + ": i am here!!");
                    endLatch.countDown();
                }catch (Exception e){
                    System.out.println(e);
                }

            }).start();
        }
        long start, end;
        start = System.nanoTime();
        System.out.println("start count!!!");
        startLatch.countDown();
        endLatch.await();
        end = System.nanoTime();
        System.out.println(end - start);
    }
    
    public static void main(String[] args) throws Exception {
        Latch test = new Latch();
        test.calc();
    }
}

```



##### CyclicBarrier

栅栏是所有线程到达栅栏位置才能继续执行, 栅栏是可以重复使用的， 初始化的时候等待n个线程和等待到n个线程运行的任务！

```java

public class Barrier {
   private final CyclicBarrier cyclicBarrier;
   public Barrier(){
       this.cyclicBarrier = new CyclicBarrier(4, () ->{
           System.out.println("this is barrier!!! end");
       });
   }
    public void meeting(){
       for(int i = 0; i < 11; i++){
           try {
               new Thread(()->{
                   System.out.println(Thread.currentThread().getName() + ": i am here!!");
                   try{
                       this.cyclicBarrier.await();
                   }catch (Exception e){
                       System.out.println(e);
                   }
               }).start();

           } catch (Exception e) {
               e.printStackTrace();
           }
       }
    }
    public static void main(String[] args) {
        Barrier barrier = new Barrier();
        barrier.meeting();
    }
}

```



##### Semphore

二值信号量相当于一把锁， 信号量可以通过， 锁和条件变量实现

```java
public class TestSemaphore {
    private final List<Integer> list;
    private final Semaphore sem;
    TestSemaphore(){
        this.list =new ArrayList<>();
        this.sem = new Semaphore(10, true);
    }
    public void addOne(){
        try {
            sem.acquire();
            list.add(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void removeOne(){
        list.remove(0);
        sem.release();
    }
    public static void main(String[] args) {
        TestSemaphore testSemaphore = new TestSemaphore();
        new Thread(()->{
            while(true){
                testSemaphore.addOne();
                System.out.println("addone cur size:" + testSemaphore.list.size());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(()->{
            while(true){
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                testSemaphore.removeOne();
                System.out.println("removeone cur size:" + testSemaphore.list.size());
            }
        }).start();

    }
}
```



##### Exchanger

exchanger 可以交换一个数据， 当交换数据的时候只要有一个线程没有到达时，会阻塞！

```java
public class TestExchanger {
    final Exchanger<String> exchanger = new Exchanger<>();
    public void a(){
        System.out.println("a is running");
        String value = "helloa";
        try {
            Thread.sleep(2000);
            this.exchanger.exchange(value);
        }catch (Exception e){
            System.out.println(e);
        }
    }
    public void b(){
        System.out.println("b is running");
        String value = "hellob";
        try {
            String temp = this.exchanger.exchange(value);
            System.out.println(value);
            System.out.println(temp);
        }catch (Exception e){
            System.out.println(e);
        }
    }
    public static void main(String[] args) {
        TestExchanger te= new TestExchanger();
        new Thread(te::a).start();
        new Thread(te::b).start();
    }
}

```



##### FetureTast

有返回值的闭锁， 新建立一个线程提前运行所要的值， 之后get的时候， 会阻塞等待值的返回！！

```java
public class TestFuture {
    private final FutureTask<String> future = new FutureTask<String>(new Callable<String>() {
        @Override
        public String call() throws Exception {
            Thread.sleep(2000);
            return "hello";
        }
    });
    private Thread thread = new Thread(future);

    public void start(){
        thread.start();
    }
    public String get(){
        try{
          return  future.get();
        }catch (Exception e){
            System.out.println(e);
            return "";
        }
    }
    public static void main(String[] args) {
        TestFuture testFuture = new TestFuture();
        testFuture.start();
        System.out.println("feture started!!!");
        System.out.println(testFuture.get());
    }
    
}
```



## 结构化并发应用程序



### 任务执行















ThreadPoolExecutor

```java
    public ThreadPoolExecutor(int corePoolSize,
                              int maximumPoolSize,
                              long keepAliveTime,
                              TimeUnit unit,
                              BlockingQueue<Runnable> workQueue,
                              ThreadFactory threadFactory,
                              RejectedExecutionHandler handler) {
        if (corePoolSize < 0 ||
            maximumPoolSize <= 0 ||
            maximumPoolSize < corePoolSize ||
            keepAliveTime < 0)
            throw new IllegalArgumentException();
        if (workQueue == null || threadFactory == null || handler == null)
            throw new NullPointerException();
        this.acc = System.getSecurityManager() == null ?
                null :
                AccessController.getContext();
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.workQueue = workQueue;
        this.keepAliveTime = unit.toNanos(keepAliveTime);
        this.threadFactory = threadFactory;
        this.handler = handler;
    }
```





**五、Queuing 队列**

BlockingQueu用于存放提交的任务，队列的实际容量与线程池大小相关联。

- 如果当前线程池任务线程数量小于核心线程池数量，执行器总是优先创建一个任务线程，而不是从线程队列中取一个空闲线程。
- 如果当前线程池任务线程数量大于核心线程池数量，执行器总是优先从运行线程队列中取一个空闲线程，而不是创建一个任务线程。
- 如果当前线程池任务线程数量大于核心线程池数量，且运行队列中无空闲任务线程，将会创建一个任务线程，直到超出maximumPoolSize，当超出maxinumPoolSize 时， 放入队列， 如果此时队列是有界的



主要有三种队列策略：

1. **Direct handoffs 直接握手队列**
    Direct handoffs 的一个很好的默认选择是 SynchronousQueue，它将任务交给线程而不需要保留。这里，如果没有线程立即可用来运行它，那么排队任务的尝试将失败，因此将构建新的线程。
    此策略在处理可能具有内部依赖关系的请求集时避免锁定。Direct handoffs 通常需要无限制的maximumPoolSizes来避免拒绝新提交的任务。
    
    **注意: **当任务持续以平均提交速度大余平均处理速度时，会导致线程数量会无限增长问题。
    
2. **Unbounded queues 无界队列**
    当所有corePoolSize线程繁忙时，使用无界队列（例如，没有预定义容量的LinkedBlockingQueue）将导致新任务在队列中等待，从而导致maximumPoolSize的值没有任何作用。当每个任务互不影响，完全独立于其他任务时，这可能是合适的; 例如，在网页服务器中， 这种队列方式可以用于平滑瞬时大量请求。**但得注意，当任务持续以平均提交速度大余平均处理速度时，会导致队列无限增长问题。**
3. **Bounded queues 有界队列**
    一个有界的队列（例如，一个ArrayBlockingQueue）和有限的maximumPoolSizes配置有助于防止资源耗尽，但是难以控制。队列大小和maximumPoolSizes需要 相互权衡：

- 使用大队列和较小的maximumPoolSizes可以最大限度地减少CPU使用率，操作系统资源和上下文切换开销，但会导致人为的低吞吐量。如果任务经常被阻塞（比如I/O限制），那么系统可以调度比我们允许的更多的线程。
- 使用小队列通常需要较大的maximumPoolSizes，这会使CPU更繁忙，但可能会遇到不可接受的调度开销，这也会降低吞吐量。



**Rejected tasks 拒绝任务**
 拒绝任务有两种情况：1. 线程池已经被关闭；2. 任务队列已满且maximumPoolSizes已满；
 无论哪种情况，都会调用RejectedExecutionHandler的rejectedExecution方法。预定义了四种处理策略：

1. **AbortPolicy**：默认测策略，抛出RejectedExecutionException运行时异常；
2. **CallerRunsPolicy**：这提供了一个简单的反馈控制机制，可以减慢提交新任务的速度；
3. **DiscardPolicy**：直接丢弃新提交的任务；
4. **DiscardOldestPolicy**：如果执行器没有关闭，队列头的任务将会被丢弃，然后执行器重新尝试执行任务（如果失败，则重复这一过程）；
    我们可以自己定义RejectedExecutionHandler，以适应特殊的容量和队列策略场景中。

