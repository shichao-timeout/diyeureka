# just for fun
写个注册中心吧

##  参考资料
https://nacos.io/zh-cn/docs/what-is-nacos.html  
https://github.com/alibaba/spring-cloud-alibaba   
https://github.com/Netflix/eureka  
https://github.com/apache/zookeeper   

### 目标
分布式、高可用、可伸缩的服务注册中心，保证服务注册与发现都是秒级以内，可以承载大规模服务实例，高并发的访问，数据一致性，数据不丢失，高可用，高可靠，可伸缩，随时可以扩容。  

#### raft协议与leader的选举    
启动了之后，3台机器，每个人都会投票给自己，选举自己当选为leader，他对自己的投票会发给其他的机器  
机器01：投票自己，机器02，机器03  
机器02：投票自己，机器01，机器03  
机器03：投票自己，机器01，机器02  
选不出来leader，3台机器，最起码需要2条机器，大多数人都认可你当leader，你才可以当选为leader  
Raft协议，如果一轮投票，发现大家没有选举出来一个leader，此时如何呢？大家都走一个随机时间的等待，timeout时间过后，再次发起第二轮选举。机器01选择休眠等待3秒钟，机器02选择休眠等待1.5秒钟，机器03选择休眠等待4秒钟      
第二轮选举的时候，机器02先苏醒过来，发现进入了第二轮投票，他发现此时没有人发送选票给他，他就还是选举自己当做leader，发送给了机器01和机器03  
机器02：投票给自己，机器02，机器02   
机器01：机器02，投票给机器02，机器02  
机器03：机器02，机器02，投票给机器02    
大家发现选票都投完了，发现超过半数的人（全票）都投给了机器02，此时机器02当选为leader，Raft协议本质就是通过随机休眠时间保证一定会在某一轮中投票出来一个人当选为leader

#### 2PC与过半写机制  
刚开始leader受到一个注册请求，uncommitted，他把这个uncommitted请求发送给各个follower，作为第一个阶段，各个follower收到uncommitted请求之后，  
返回ack给leader，如果leader收到了超过半数的follower的ack，此时leader就把这个请求标记为committed，同时发送committed请求给所有的follower，让他们也对消息进行committed  

#### Leader的崩溃恢复
如果注册的请求还没发送过去，leader就崩溃了，此时进入了在剩下的两台follower中重新选举leader过程中，还没有诞生一个新的leader，服务注册的请求是失败的，无法发送，需要不停的重试  
如果注册请求发送到了一个leader上去，此时uncommitted状态，没有发送uncommitted请求到其他follower上去，leader崩溃了，此时这个注册请求是失败的，需要服务不停的重试，选举出来新的Leader  
如果注册请求发送到了leader上去，也发送了uncommitted请求到其他follower上去，部分follower收到了请求，但是还没达到半数follower返回ack给Leader，leader就挂了，会导致服务也认为服务注册强求失败了  
如果已经超过了半数的uncommitted请求的ack给leader了，服务注册请求已经成功了，此时leader崩溃了，选举一个新的leader，会去接收其他follower的ack，如果超过半数follower有ack，直接commit操作  

#### nacos源码分析

#### 数据如何存储
总体思路：借鉴ES和redis cluster的思路，对数据做分片，整个数据集被分成多个shard，分布在多台机器上。    
考虑如何复制数据的问题？同步or异步？多种数据复制的策略？  
容灾与备份：预写日志（WAL）和数据快照。  
定位数据的分片位置：哈希取模。hash(ip+port)%shard_num,这里初始化分片数量为256.那么就涉及到两层对应关系：服务与shard，shard与机器。这里
借鉴kafka的做法，有个controller的角色，来管理集群有多少台机器，以及shard如何在这些机器上的分配策略。新的服务注册的时候，首先连接到controller，
然后controller指定服务应该注册到哪台机器上。  
controller的选举问题：引入controller又涉及到controller的选举问题：基于raft协议来在master中选举出controller。master成为controller后，
需要定期发送心跳给其他master节点，当其他master节点发现controller挂了后，需要重新选举。  

#### 客户端如何更新数据
两种模式：推模式和拉模式。  
nacos和eureka采取拉模式，第一次是全量拉取，后面增量拉取，对性能友好。  
zk采取推模式，采取监听的方式，有新的变动以事件的形式推给客户端，推模式每次变动都会推全量数据。   
 
#### server与client
server用于部署成集群，client用于嵌入到各种client端


 
 



  

  








