# Captcha_generator

Консольное приложение для демонстрации возможностей пакета CaptchaGenerator -
генерации графической CAPTCHA.

## Принцип работы

За основу взят принцип послойной генерации результирующего изображения.
Слой принимает на вход изображение на определенном этапе его генерации, вносит свои изменения
и возвращает полученный результат. Таким образом, генерация всей капчи сводится
к последовательному вызову генерации каждого слоя, входящего в генерацию

## Использование консольного интерфейса

```
captcha_generator -layers [-directory] [-count]
```
* **-layers** (_required_) - псевдонимы слоев, используемых для генерации (см. "_Слои_").
Порядок слоев имеет значение.
* **-directory** (_optional_) (default="**./generated**") - директория, куда будут
сохраняться сгенерированные изображения
* **-count** (_optional_) (default=**100**) - количество изображений для генерации

## Пример использования CaptchaGenerator

```java
CaptchaGenerator generator = new CaptchaGenerator(new CaptchaGenerator.Options());

generator.setLayers(new IRenderable[]{
new Base(new Base.Options()),
new Waves(new Waves.Options()),
new NoiseFill(new NoiseFill.Options())
});

generator.generate(100, "./generated")
```

## Слои

### base

Слой base является базовым для последующих искажений. Представляет из себя набор разноцветных
символов заданной длины из разных шрифтов, повернутых на небольшой угол и смещенных по обеим
осям на небольшие величины - для базовой защиты от машинного чтения.

<details>
<summary>Пример</summary>

![1ieoU1](.\src\horockey\samples\Base\1ieoU1.png)
![NA6vfz](.\src\horockey\samples\Base\NA6vfz.png)
![voK8Np](.\src\horockey\samples\Base\voK8Np.png)
![weWq1S](.\src\horockey\samples\Base\weWq1S.png)
![XC6UsH](.\src\horockey\samples\Base\XC6UsH.png)

</details>

### noise_fill

Слой noise_circuit заменяет текущий фон изображения на линейный шум. Для корректного отделения
фона от пикселей полезной нагрузки на данном этапе применяется обычный **трешхолд** - то есть
должна существовать барерная яркость, которая будет выше яркости любого фонового пикселя,
но при этом меньше яркости любого целевого пикселя.

<details>
<summary>Пример</summary>

![7SbOtI](.\src\horockey\samples\Noise_fill\7SbOtI.png)
![dgaOMI](.\src\horockey\samples\Noise_fill\dgaOMI.png)
![mPZ2gN](.\src\horockey\samples\Noise_fill\mPZ2gN.png)
![nmYa4l](.\src\horockey\samples\Noise_fill\nmYa4l.png)
![zD8P0G](.\src\horockey\samples\Noise_fill\zD8P0G.png)

</details>

### noise_circuit

Слой noise_circuit заменяет все пиксели, кроме _граничных_ на линейный шум. _Граничный пиксель_ -
это пиксель, яркость которого выше заданной барьерной (то есть он целевой), и в его двумерной
ε-окрестности находится пиксель с яркостью ниже заданной барьерной (фоновый). В результате любой
достаточно жирный контур становится "дырявым", что в значительной степени снижает эффективность
машинного разпозавания текста, оставляя его относительно легко распознаваемым человеком.

<details>
<summary>Пример</summary>

![3qojMG](.\src\horockey\samples\Noise_circuit\3qojMG.png)
![5rGcwS](.\src\horockey\samples\Noise_circuit\5rGcwS.png)
![eXeW75](.\src\horockey\samples\Noise_circuit\eXeW75.png)
![fCO3WK](.\src\horockey\samples\Noise_circuit\fCO3WK.png)
![wPtnqr](.\src\horockey\samples\Noise_circuit\wPtnqr.png)

</details>

### gradient_fill

Слой gradient_fill представляет изображение в формате градиент-на-градиенте.
С заданными ограничениями генерируется сэмпл двуточечной градиентной заливки. Целевы пиксели
заменяются на пиксели сэмпла из одного региона, а фоновые - из другого региона. В результате
задача по выделению компонент связности для распознания становится гораздо более трудноразрешимой.

<details>
<summary>Пример</summary>

![2pLXzO](.\src\horockey\samples\Gradient_fill\2pLXzO.png)
![5UIkUw](.\src\horockey\samples\Gradient_fill\5UIkUw.png)
![5xyoWm](.\src\horockey\samples\Gradient_fill\5xyoWm.png)
![T0OTj5](.\src\horockey\samples\Gradient_fill\T0OTj5.png)
![Y8KJ2o](.\src\horockey\samples\Gradient_fill\Y8KJ2o.png)

</details>

### gradient_circuit
Слой gradient_circuit действует по аналогии с gradient_fill, с той лишь разницей, что разделение
идет не по признаку фоновый - целевой, а по признаку _граничный_ - _не граничный_ (см. выше)

<details>
<summary>Пример</summary>

![1ZCsNg](.\src\horockey\samples\Gradient_circuit\1ZCsNg.png)
![581aVv](.\src\horockey\samples\Gradient_circuit\581aVv.png)
![jVMSL5](.\src\horockey\samples\Gradient_circuit\jVMSL5.png)
![rMIkse](.\src\horockey\samples\Gradient_circuit\rMIkse.png)
![wy3rWj](.\src\horockey\samples\Gradient_circuit\wy3rWj.png)

</details>

### Twirl

Слой twirl применяет к изображению нелинейные скручивающие искажения. В некотором количестве
точек скручивания пиксели исходного изображения смещаются в полярной системе координат на
заданный угол. При этом смещение происходит, естесственно не на фиксированную величину -
сила смещения зависит от радиуса скручивания и текущего модуля точки в полярной СК относительно
центра кручивания.

<details>
<summary>Пример</summary>

![2PR71o](.\src\horockey\samples\Twirl\2PR71o.png)
![6uEM3h](.\src\horockey\samples\Twirl\6uEM3h.png)
![aGQXjN](.\src\horockey\samples\Twirl\aGQXjN.png)
![CKkMv2](.\src\horockey\samples\Twirl\CKkMv2.png)
![wpWAkU](.\src\horockey\samples\Twirl\wpWAkU.png)

</details>

### Waves

Слой waves применяет к изображению нелинейные волновые искажения. Пиксели исходного изображения
смещаются на некоторую гармоническую функцию по обеим осям. Слой waves имеет одну
из самых высоких эффективностей по усложнению задачи автоматического распознания текста, не
ухудшая в значительной степени человекочитаемость изображения.

<details>
<summary>Пример</summary>

![806W2Z](.\src\horockey\samples\Waves\806W2Z.png)
![D47WA1](.\src\horockey\samples\Waves\D47WA1.png)
![hUznRN](.\src\horockey\samples\Waves\hUznRN.png)
![KTbdrG](.\src\horockey\samples\Waves\KTbdrG.png)
![ZrcLib](.\src\horockey\samples\Waves\ZrcLib.png)

</details>
