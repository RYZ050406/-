# 阻止狗头起飞 MC 模组工作区

这个仓库用于维护 Minecraft `1.21.11` 的地图配套 Fabric 模组。根目录是工作区，不再直接放某一个模组的源码；每个模组都放在 `mods/` 下的独立目录里。

## 目录

```text
mods/
  flyable/        生存模式飞行粘液球
  perseverance/   起飞超人 Boss 与毅力之证
  connection/      屎、超级答辩、投掷物与果蔬粉合成
  boss/            Boss 武器、靴子与 BOSS 模式
```

根目录保留 Gradle Wrapper：

```text
gradlew
gradlew.bat
gradle/wrapper/
```

## 环境

- Minecraft `1.21.11`
- Fabric Loader `0.19.3`
- Fabric API `0.141.6+1.21.11` 或兼容版本
- JDK 21

本机可使用 Minecraft 自带 Java：

```powershell
$env:JAVA_HOME='C:\Users\ASUS\AppData\Roaming\.minecraft\runtime\java-runtime-delta'
```

## 构建

在仓库根目录构建全部模组：

```powershell
.\gradlew.bat build
```

单独构建某个模组：

```powershell
.\gradlew.bat buildFlyable
.\gradlew.bat buildPerseverance
.\gradlew.bat buildConnection
.\gradlew.bat buildBoss
```

也可以进入模组目录构建：

```powershell
cd mods\flyable
..\..\gradlew.bat build
```

## 产物

```text
mods/flyable/build/libs/flyable-1.0.0.jar
mods/perseverance/build/libs/perseverance-1.0.0.jar
mods/connection/build/libs/connection-1.0.0.jar
mods/boss/build/libs/boss-1.0.0.jar
```

这些 jar 都不内置 Fabric API。使用时需要把 Fabric API 单独放进客户端或服务端的 `mods` 文件夹。

## 模组

`flyable`：

- `/give @p flyable:flight_slime_ball`
- `/give @p flyable:temporary_flight_slime_ball`
- 普通飞行粘液球可切换无限高速飞行。
- 限时飞行粘液球可飞行最多 10 秒，结束后进入 30 秒冷却。

`perseverance`：

- `/give @p perseverance:red_nether_star`
- `/give @p perseverance:perseverance_proof`
- 红色下界之星右键地面召唤 Boss“起飞超人”。
- 起飞超人血量 1000，攻击伤害 5，每秒攻击一次，攻击距离 4 格，显示 Boss 血条。
- 起飞超人使用 TheGou_ 皮肤，身穿钻套并手持钻剑作为装饰，死亡掉落“毅力之证”。

`connection`：

- `/give @p connection:poop 87`
- `/give @p connection:super_poop`
- `/give @p connection:fruit_vegetable_powder`
- 默认不会自动拉屎；按 K 可以只为自己切换拉屎模式。开启后，玩家按住 shift 时每秒必定掉落 1 个“屎”，每秒最多掉落 1 个。
- 不蹲下时右键“屎”会像鸡蛋一样丢出，命中实体造成 3 点伤害。
- 蹲下时右键“屎”会吃下去，恢复 3 格饱食度，并获得 60 秒反胃 I、中毒 I、饱和 I。
- 五个“屎”在工作台按十字排列可合成棕色加粗、带附魔光效的“超级答辩”。
- 不蹲下时右键“超级答辩”会丢出，命中实体造成 7 点伤害；蹲下时右键会吃下去，恢复 20 点饱食度且没有负面效果。
- 工作台中任意位置放入 3 格水果、3 格蔬菜和 1 格 87 个“屎”，可合成 1 个“一罐果蔬粉”。

`boss`：

- `/give @p boss:sword_one`
- `/give @p boss:sword_two`
- `/give @p boss:sword_three`
- `/give @p boss:sword_four`
- `/give @p boss:sword_five`
- `/give @p boss:dog_king_sword`
- `/give @p boss:leaping_leather_boots`
- `/give @p boss:god_netherite_boots`
- `/give @p boss:boss_mode_star`
- 五把不可破坏的剑基础伤害 5，攻击无 CD；右键会在前方 10 格生成对应伪方块球体，并触发不破坏地形、无伤害、只击退的爆炸效果。
- 狗王剑不可破坏，基础伤害 10，主手持有时移动速度 +0.05；右键射出 40 格激光，命中活体造成 20 点可被护甲和抗性削减的普通伤害，CD 30 秒。
- 每把剑独立记录 10 秒技能 CD，手持时会在经验条上方显示冷却或“已就绪”。
- 伪绿宝石块/伪粉色羊毛会给玩家 1 秒缓慢 I，伪红石块会灼烧 1 秒，伪紫水晶块会给 1 秒挖掘疲劳 I，伪黑曜石会给 1 秒反胃 I。
- 皮革靴子不可破坏，提供跳跃高度约 +2 格、护甲 +3 格、速度 +0.03。
- 下界合金靴子不可破坏，提供跳跃高度约 +2 格、护甲 +10 格、速度 +0.05，并获得抗性提升 II。
- 绿色下界之星右键进入 BOSS 模式，所有玩家可见顶部 BOSS 血条，死亡后退出。

