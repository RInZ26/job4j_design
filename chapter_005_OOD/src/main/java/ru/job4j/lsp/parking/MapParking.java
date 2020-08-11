package ru.job4j.lsp.parking;

import java.util.*;
import java.util.stream.Collectors;

public class MapParking extends DefaultParking {
    /**
     * mapBase: 0 - легкая машина, 1 - грузовая машина, -1 - препятствие
     * 00110
     * 11001
     * 010-1-1
     * mapActual: 0 - ячейка свободна, 1 - ячейка занята
     * 01000
     * 11111
     * 00000
     * должен быть вариант с Empty
     */
    /**
     * Содержит машину и координаты где машина расположена, в случае, если
     * машина большая, то возможно два варианта:
     * Координы указывают на ячейку, которая является специальной для больших
     * машин или указывается на ячейку, где стоят лайтовые машины, в таком
     * случае, необходимо сверяться с StructureMap, чтобы по getSize()
     * понимать, сколько ячеек она занимает
     */
    private Map<Car, int[]> mapCar = new HashMap<>();

    /**
     * Содержит структуру парковки
     */
    private int[][] mapStructure;
    /**
     * Текущее состояние парковки
     */
    private int[][] mapActual;

    public MapParking(int lightAmount, int highAmount) {
        super(lightAmount, highAmount);
        initDefaultMaps();
    }

    public MapParking(int[][] mapStructure) {
        super(mapStructure);
        this.mapStructure = mapStructure;
    }

    /**
     * Космический метод, который считает количество одинаковых чисел
     * (наверняка есть куда проще через intStream, но "проще" было черех
     * объектный) всё равно метод для теста //fixme delete
     *
     * @param mapStructure
     *
     * @return
     */
    public static Map<Integer, Integer> figureAmountsByMapStructure(
            int[][] mapStructure) {
        return Arrays.stream(mapStructure)
                     .flatMapToInt((arr) -> Arrays.stream(arr))
                     .boxed()
                     .collect(Collectors.toMap(num -> num, val -> 1,
                                               (a, b) -> a + b));
    }

    /**
     * Метод, заполняющий в простом виде текущую карту парковки
     * по переданному колву автомобилией и их типу, а так же заполняет мапу
     * актуальную с учётом блоков
     */
    private int initDefaultMaps() {
        int size = (int) Math.ceil(Math.sqrt(lightAmount + highAmount));
        mapStructure = new int[size][size];
        mapActual = new int[size][size];
        int lightCount = lightAmount;
        int highCount = highAmount;
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if (lightCount > 0) {
                    mapStructure[r][c] = 0;
                    lightCount--;
                } else if (highCount > 0) {
                    mapStructure[r][c] = 1;
                    highCount--;
                } else {
                    mapStructure[r][c] = -1;
                    mapActual[r][c] = -1;
                }
            }
        }
        return size;
    }

    public int[][] getMapActual() {
        return mapActual;
    }

    public int[][] getMapStructure() {
        return mapStructure;
    }

    /**
     * Метод, ради которого всё и писалось:
     * Возможность кастомной генерации парковки с любыми условиями
     */
    public void setMapStructure(int[][] mapStructure) {
        this.mapStructure = mapStructure;
    }

    @Override
    public boolean isCanBeParking(Car car) {
        return car.getSize() > 1 ? isBigCarCanBeParking(car)
                : isSmallCarCanBeParking();
    }

    private boolean isBigCarCanBeParking(Car car) {
        if (freeHighAmount > 0) {
            return true;
        } else if (freeLightAmount < car.getSize()) {
            return false;
        } else {
            return findFreeSpace(car.getSize()) != null;
        }
    }

    private boolean isSmallCarCanBeParking() {
        return freeLightAmount > 0;
    }

    /**
     * Ищет координаты в которую можно положить машину (с заполнением вправо,
     * если нужно), то есть если вернулось 0,1, а у машины размер 2, то
     * подразумевается, что ячейка 0,2 тоже свободна;
     *
     * @return массив координат / null, если размещение невозможно
     */
    int[] findFreeSpace(int size) {
        for (int r = 0; r < mapActual.length; r++) {
            for (int c = 0; c < mapActual[r].length; c++) {
                switch (mapActual[r][c]) {
                    case 0:
                        if (mapActual[r].length > c + size - 1) {
                            boolean isFree = true;
                            for (int i = c + 1; i < c + size; i++) {
                                if (mapActual[r][i] != 0) {
                                    isFree = false;
                                    break;
                                }
                            }
                            if (isFree) {
                                return new int[]{r, c};
                            }
                        }
                        break;
                    case 1:
                        continue;
                    default:
                        break;
                }
            }
        }
        return null;
    }

    /**
     * Ищет свободную ячейку опередленного типа в mapStructure, у нас это пока
     * что числа, вида
     * 0 - лайтовая, 1 - тяжелая, потом проверяем - свободна ли она в mapActual
     *
     * @param type тип, который ищем
     *
     * @return массив координат / null, если размещение невозможно
     */
    int[] findNextFreeType(int type) {
        for (int r = 0; r < mapActual.length; r++) {
            for (int c = 0; c < mapActual[r].length; c++) {
                if (mapStructure[r][c] == type) {
                    if (mapActual[r][c] == 0) { //если свободна
                        return new int[]{r, c};
                    }
                }
            }
        }
        return null;
    }

    @Override
    public boolean addCar(Car car) {
        if (car.getSize() == 1) {
            return addLightCar(car);
        } else {
            return addHighCar(car);
        }
    }

/*
        *//**
         * Перегружаем метод для возможности добавлять ещё и по координатам,
         * передаваемые в качестве x,y.
         * Сначала проверяем свободна ли ячейка вообще

    public boolean addCar(Car car, int x, int y) {
        if (x >= mapActual.length || y >= mapActual[x].length) {
            throw new IllegalArgumentException("Недопустимые координаты");
        }
        if (mapActual[x][y] == 0) {
            int type = mapStructure[x][y];
            if (type == 0 && car.getSize() == 1) {
                updateMapActual(x, y, car.getSize(), false);

            } else if (car.getSize() > 1) {
                if (type == 1) {
                    updateMapActual(x, y, 1, false);
                } else if (type == 0) {

                }
            }
        }
    }*/

    /**
     * Попытка найти место для легкой машины и добавить её
     */
    private boolean addLightCar(Car car) {
        int[] coordinates;
        boolean result = false;
        coordinates = findNextFreeType(0);
        if (coordinates != null) {
            mapCar.put(car, coordinates);
            freeLightAmount--;
            updateMapActual(coordinates[0], coordinates[1], 1, false);
            result = true;
        }
        return result;
    }

    /**
     * Попытка найти место для грузовой машины и добавить её
     * Есть спец.место ? туда : ищем среди лайтовых
     */
    private boolean addHighCar(Car car) {
        int[] coordinates;
        boolean result = false;
        coordinates = findNextFreeType(1);
        if (coordinates != null) {
            mapCar.put(car, coordinates);
            freeHighAmount--;
            updateMapActual(coordinates[0], coordinates[1], 1, false);
            result = true;
        } else {
            coordinates = findFreeSpace(car.getSize());
            if (coordinates != null) {
                mapCar.put(car, coordinates);
                freeLightAmount -= car.getSize();
                updateMapActual(coordinates[0], coordinates[1], car.getSize(),
                                false);
                result = true;
            }
        }
        return result;
    }

    /**
     * Ихменяет mapActual по boolean - свободно/нет = 1/0
     */
    private void updateMapActual(int row, int column, int size,
                                 boolean isFree) {
        for (int c = column; c < column + size; c++) {
            mapActual[row][c] = isFree ? 0 : 1;
        }
    }

    /**
     * Проверка, являются ли выбранные координаты в mapStructure спец клеткой
     * (Не размера 1 для лайтовых)
     *
     * @param coordinates проверяемые координаты
     */
    private boolean isSpecialCell(int[] coordinates) {
        return mapStructure[coordinates[0]][coordinates[1]] > 1;
    }

    /**
     * Удаляет Car из коллекции с очисткой данных о себе в mapActual
     */
    @Override
    public boolean removeCar(Car key) {
        Optional<int[]> optional = Optional.ofNullable(mapCar.get(key));
        optional.ifPresent(coord -> {
            if (isSpecialCell(coord)) {
                updateMapActual(coord[0], coord[1], 1, true);
                mapCar.remove(key);
            } else {
                updateMapActual(coord[0], coord[1], key.getSize(), true);
                mapCar.remove(key);
            }
        });
        return optional.isPresent();
    }

    @Override
    public Collection<Car> getCars() {
        return mapCar.keySet();
    }
}
