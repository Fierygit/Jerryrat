<!--
 * @Author: Firefly
 * @Date: 2019-12-17 22:50:48
 * @Descripttion: 
 * @LastEditTime : 2019-12-23 20:17:16
 -->


更换清华源


```shell
conda config --add channels https://mirrors.tuna.tsinghua.edu.cn/anaconda/cloud/conda-forge/
conda config --add channels https://mirrors.tuna.tsinghua.edu.cn/anaconda/pkgs/free/
conda config --add channels https://mirrors.tuna.tsinghua.edu.cn/anaconda/pkgs/main/
conda config --append channels https://mirrors.tuna.tsinghua.edu.cn/anaconda/cloud/fastai/
conda config --append channels https://mirrors.tuna.tsinghua.edu.cn/anaconda/cloud/pytorch/
conda config --append channels https://mirrors.tuna.tsinghua.edu.cn/anaconda/cloud/bioconda/
 
# 搜索时显示通道地址
conda config --set show_channel_urls yes
```


```shell
conda env list
```


```shell
conda activate py37
```

```
activate // 切换到base环境
activate name // 切换到name环境
conda create -n learn python=3 // 创建一个名为learn的环境并指定python版本为3(的最新版本)
conda env list // 列出conda管理的所有环境
conda list // 列出当前环境的所有包
conda install requests 安装requests包
conda remove requests 卸载requets包
conda remove -n learn --all // 删除learn环境及下属所有包
conda update requests 更新requests包
conda env export > environment.yaml // 导出当前环境的包信息
conda env create -f environment.yaml // 用配置文件创建新的虚拟环境
```

