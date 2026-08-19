# connection Fabric 模组

Minecraft `1.21.11` 的 Fabric 模组，新增“屎”、可投掷的屎实体，以及“一罐果蔬粉”合成。

## 使用

需要：

- JDK 21
- 游戏客户端或服务端需要安装 Fabric API `0.141.6+1.21.11` 或兼容版本

从仓库根目录构建：

```powershell
.\gradlew.bat buildConnection
```

或进入本模组目录构建：

```powershell
cd mods\connection
..\..\gradlew.bat build
```

构建完成后，把 `mods/connection/build/libs/connection-1.0.0.jar` 放进客户端或服务端的 `mods` 文件夹。

这个构建不会内置 Fabric API，需要在 `mods` 文件夹里单独放 Fabric API。

获取物品：

```mcfunction
/give @p connection:poop 87
/give @p connection:fruit_vegetable_powder
```

玩家按住 shift 时每秒必定掉落 1 个“屎”，每秒最多掉落 1 个。

“屎”最多堆叠 87 个。不蹲下时右键会像鸡蛋一样丢出，命中实体造成 3 点伤害；蹲下时右键会吃下去，恢复 3 格饱食度，并获得 60 秒反胃 I、中毒 I、饱和 I。

工作台中任意位置放入 3 格水果、3 格蔬菜和 1 格 87 个“屎”，可合成 1 个“一罐果蔬粉”。水果和蔬菜范围由 `data/connection/tags/item/fruits.json` 与 `data/connection/tags/item/vegetables.json` 控制。
