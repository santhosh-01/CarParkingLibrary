package model;

import java.time.LocalTime;
import java.util.ArrayList;

public class ParkingLot {

    private final int floorNo;
    private final int row;
    private final int col;
    private final ArrayList<ArrayList<Cell>> mat;
    private int vacancy;
    private final int inBetweenPathWidth;
    private static int parkingPlaceNumber = 1;

    public ParkingLot(int floor, int row, int col,int inBetweenPathWidth) {
        vacancy = row * col;
        this.floorNo = floor;
        this.row = row + (((row - 1)/2)+2) * inBetweenPathWidth;
        this.col = col + (2 * inBetweenPathWidth);
        this.mat = new ArrayList<>();
        this.inBetweenPathWidth = inBetweenPathWidth;
        formatMatrix();
    }

    private void formatMatrix() {
        if(inBetweenPathWidth == 1) {
            for (int i = 0; i < row; ++i)
            {
                ArrayList<Cell> cells = new ArrayList<>();
                for(int j = 0; j < col; ++j)
                {
                    if(i == row - 1 || i % 3 == 0 || j == 0 || j == col - 1) {
                        PathCell pathCell = new PathCell();
                        cells.add(pathCell);
                    }
                    else {
                        ParkingCell parkingCell = new ParkingCell();
                        parkingCell.setPosition(parkingPlaceNumber);
                        parkingPlaceNumber++;
                        parkingCell.setIsParked("E");
                        cells.add(parkingCell);
                    }
                }
                this.mat.add(cells);
            }
        }
        else if(inBetweenPathWidth == 2){
            for (int i = 0; i < row; ++i)
            {
                ArrayList<Cell> cells = new ArrayList<>();
                for(int j = 0; j < col; ++j)
                {
                    if(i <= 1 || i >= row - 2 || i % 4 == 0 || i % 4 == 1 || j <= 1 || j >= col - 2) {
                        PathCell pathCell = new PathCell();
                        cells.add(pathCell);
                    }
                    else {
                        ParkingCell parkingCell = new ParkingCell();
                        parkingCell.setPosition(parkingPlaceNumber);
                        parkingPlaceNumber++;
                        parkingCell.setIsParked("E");
                        cells.add(parkingCell);
                    }
                }
                this.mat.add(cells);
            }
        }
    }

    public int getVacancy() {
        return vacancy;
    }

    public int getFloorNo() {
        return floorNo;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean isParkingFull() {
        return this.vacancy == 0;
    }

    public int getFirstParkingPosition() {
        for(int i = 0; i < row; ++i) {
            for (int j = 0; j < col; ++j) {
                if(this.mat.get(i).get(j).getCellValue().equals("-")) {
                    return mat.get(i).get(j).getPosition();
                }
            }
        }
        return -1;
    }

    public CarParkingPlace getNearestParkingPosition(int r, int c) {
        int[][] ans = new int[row][col];

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                ans[i][j] = Integer.MAX_VALUE;
            }
        }

        // For each cell
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                ans[i][j] = Math.abs(i - r) + Math.abs(j - c);
            }
        }

        int ctr = 0;
        while (true) {
            boolean flag = false;
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < col; j++) {
                    if(ans[i][j] == ctr) {
                        flag = true;
                        if (isValidEmptyParkingPlace(i, j)) {
                            return new CarParkingPlace(i,j);
                        }
                    }
                }
            }
            if(!flag) break;
            ctr++;
        }
        return null;
    }

    public CarParkingPlace getCarNumberPosition(String CarNo) {
        for(int i = 0; i < row; ++i) {
            for (int j = 0; j < col; ++j) {
                Car car = null;
                if(this.mat.get(i).get(j) instanceof ParkingCell) {
                    car = ((ParkingCell)this.mat.get(i).get(j)).getCar();
                }
                if(car != null && car.getCarNumber() != null && car.getCarNumber().equals(CarNo)) {
                    return new CarParkingPlace(i,j);
                }
            }
        }
        return null;
    }

    public void showParkingLot() {
        System.out.println();
        for(int i = 0; i < row; ++i)
        {
            for(int j = 0; j < col; ++j)
            {
                if(mat.get(i).get(j).getCellValue().equals("-") || mat.get(i).get(j).getCellValue().equals("X")) {
                    System.out.printf("%8s ",mat.get(i).get(j).getCellValue());
                }
                else System.out.printf("%8s ",mat.get(i).get(j).getCellValue());
            }
            System.out.println();
        }
    }

    public ParkingCell exitACar(CarParkingPlace pos) {
        ParkingCell parkingCell = ((ParkingCell)mat.get(pos.getRow()).get(pos.getCol()));
        parkingCell.setCarExitTime(LocalTime.now());
        ParkingCell parkingCell1 = new ParkingCell(parkingCell);
        parkingCell.setCar(null);
        parkingCell.setCellValue("-");
        parkingCell.setParkedTime(null);
        parkingCell.setCarExitTime(null);
        parkingCell.setIsParked("E");
        this.vacancy ++;
        return parkingCell1;
    }

    public void showModifiedParkingLot(boolean isDescriptionNeeded) {
        System.out.println();
        for(int i = 0; i < row; ++i)
        {
            for(int j = 0; j < col; ++j)
            {
                if(mat.get(i).get(j).getCellValue().equals("X")) {
                    System.out.printf("%8s ",mat.get(i).get(j).getCellValue());
                }
                else System.out.printf("%8s ",mat.get(i).get(j).getPosition() + "-" +
                        ((ParkingCell)mat.get(i).get(j)).getIsParked());
            }
            System.out.println();
        }
        if(isDescriptionNeeded) {
            System.out.println("\nHere, All Parking Places represented by R/C-E or R/C-P");
            System.out.println("R means Rth Rows, C means Cth Column");
            System.out.println("E stands for Empty, P stands for Parked");
        }
    }

    public void showDetailedPath() {
        System.out.println();
        for(int i = 0; i < row; ++i)
        {
            for(int j = 0; j < col; ++j)
            {
                if(mat.get(i).get(j).getCellValue().equals("X")) {
                    System.out.printf("%8s ", mat.get(i).get(j).getCellValue() +
                            ((PathCell)mat.get(i).get(j)).getDirection());
                }
                else {
                    System.out.printf("%8s ", mat.get(i).get(j).getCellValue());
                }
            }
            System.out.println();
        }
        System.out.println("\nHere, Car path represented by R/C{D}");
        System.out.println("R means Rth Rows, C means Cth Column and " +
                "D represents Direction that the car can travel to reach parking place");
    }

    public void setDetailedLeftEntryPath(int r, int c) {
        if(mat.get(r+1).get(c).getCellValue().equals("X")) {
            int j = 0;
            for(int i = row - 1; i > r+1; --i) {
                if (mat.get(i).get(j) instanceof PathCell) {
                    ((PathCell) mat.get(i).get(j)).setDirection("{^}");
                }
            }
            for(; j < c; ++j) {
                if(mat.get(r + 1).get(j) instanceof  PathCell) {
                    ((PathCell)mat.get(r + 1).get(j)).setDirection("{>}");
                }
            }
            if(mat.get(r + 1).get(c) instanceof  PathCell) {
                ((PathCell)mat.get(r + 1).get(c)).setDirection("{^}");
            }
        }
        else {
            int i = row - (inBetweenPathWidth);
            int j = col - (inBetweenPathWidth);
            if(mat.get(row - 1).get(0) instanceof  PathCell) {
                ((PathCell)mat.get(row - 1).get(0)).setDirection("{^}");
            }
            for(int j1 = 0 ; j1 < j ; ++j1) {
                if(mat.get(i).get(j1) instanceof  PathCell) {
                    ((PathCell)mat.get(i).get(j1)).setDirection("{>}");
                }
            }
            for(int i1 = i; i1 > r - 1; --i1) {
                if(mat.get(i1).get(j) instanceof  PathCell) {
                    ((PathCell)mat.get(i1).get(j)).setDirection("{^}");
                }
            }
            for(int j1 = j; j1 > c; --j1) {
                if(mat.get(r-1).get(j) instanceof  PathCell) {
                    ((PathCell)mat.get(r-1).get(j1)).setDirection("{<}");
                }
            }
            if(mat.get(r-1).get(c) instanceof  PathCell) {
                ((PathCell)mat.get(r-1).get(c)).setDirection("{v}");
            }
        }
    }

    public void setDetailedEntryPath(int r, int c) {
        int j = inBetweenPathWidth - 1;
        if(mat.get(r-1).get(c).getCellValue().equals("X")) {
            for(int i = row - 1; i >= r; --i) {
                if (mat.get(i).get(j) instanceof PathCell) {
                    ((PathCell) mat.get(i).get(j)).setDirection("{^}");
                }
            }
            for(; j < c; ++j) {
                if(mat.get(r - 1).get(j) instanceof  PathCell) {
                    ((PathCell)mat.get(r - 1).get(j)).setDirection("{>}");
                }
            }
            if(mat.get(r - 1).get(c) instanceof  PathCell) {
                ((PathCell)mat.get(r - 1).get(c)).setDirection("{v}");
            }
        }
        else {
            int to = col - (inBetweenPathWidth);
            for(int i = row - 1; i >= r - 1; --i) {
                if (mat.get(i).get(j) instanceof PathCell) {
                    ((PathCell) mat.get(i).get(j)).setDirection("{^}");
                }
            }
            for(; j < to; ++j) {
                if(mat.get(r - 2).get(j) instanceof  PathCell) {
                    ((PathCell)mat.get(r - 2).get(j)).setDirection("{>}");
                }
            }
            for(int i = r - 2; i <= r; ++i) {
                if(mat.get(i).get(to) instanceof  PathCell) {
                    ((PathCell)mat.get(i).get(to)).setDirection("{v}");
                }
            }
            for(; j > c; --j) {
                if(mat.get(r + 1).get(j) instanceof  PathCell) {
                    ((PathCell)mat.get(r + 1).get(j)).setDirection("{<}");
                }
            }
            if(mat.get(r + 1).get(c) instanceof  PathCell) {
                ((PathCell)mat.get(r + 1).get(c)).setDirection("{^}");
            }
        }
    }

    public void setDetailedLeftExitPath(int r, int c) {
        int j = inBetweenPathWidth - 1;
        int jj = col - (inBetweenPathWidth);
        if(mat.get(r+1).get(c).getCellValue().equals("X")) {
            int i = r - 2;
            if(mat.get(r-1).get(c).getCellValue().equals("X")) i = r - 1;
            for(int j1 = c; j1 < jj; ++j1) {
                if(mat.get(r+1).get(j1) instanceof PathCell) {
                    ((PathCell)mat.get(r+1).get(j1)).setDirection("{>}");
                }
            }
            for(int i1 = r+1; i1 > i; --i1) {
                if(mat.get(i1).get(jj) instanceof PathCell) {
                    ((PathCell)mat.get(i1).get(jj)).setDirection("{^}");
                }
            }
            for(int j1 = jj; j1 > j; --j1) {
                if(mat.get(i).get(j1) instanceof PathCell) {
                    ((PathCell)mat.get(i).get(j1)).setDirection("{<}");
                }
            }
            for(int i1 = i; i1 < row; ++i1) {
                if(mat.get(i1).get(j) instanceof PathCell) {
                    ((PathCell)mat.get(i1).get(j)).setDirection("{v}");
                }
            }
        }
        else {
            for(int j1 = c; j1 > j; --j1) {
                if(mat.get(r-1).get(j1) instanceof PathCell) {
                    ((PathCell)mat.get(r-1).get(j1)).setDirection("{<}");
                }
            }
            for(int i1 = r - 1; i1 < row; ++i1) {
                if(mat.get(i1).get(j) instanceof PathCell) {
                    ((PathCell)mat.get(i1).get(j)).setDirection("{v}");
                }
            }
        }
    }

    public void setDetailedExitPath(int r, int c) {
        int i = row - (inBetweenPathWidth);
        int j = col - (inBetweenPathWidth);
        if(mat.get(r-1).get(c).getCellValue().equals("X")) {
            for(int j1 = c; j1 < j; ++j1) {
                if(mat.get(r - 1).get(j1) instanceof  PathCell) {
                    ((PathCell)mat.get(r - 1).get(j1)).setDirection("{>}");
                }
            }
            for(int i1 = r - 1; i1 < i; ++i1) {
                if(mat.get(i1).get(j) instanceof  PathCell) {
                    ((PathCell)mat.get(i1).get(j)).setDirection("{v}");
                }
            }
            for(int j1 = j; j1 >= 0; --j1) {
                if(mat.get(i).get(j1) instanceof  PathCell) {
                    ((PathCell)mat.get(i).get(j1)).setDirection("{<}");
                }
            }
            for(int i1 = i; i1 < row; ++i1) {
                if(mat.get(i1).get(0) instanceof  PathCell) {
                    ((PathCell)mat.get(i1).get(0)).setDirection("{v}");
                }
            }
        }
        else {
            for(int j1 = c; j1 > 0; --j1) {
                if(mat.get(r+1).get(j1) instanceof PathCell) {
                    ((PathCell)mat.get(r+1).get(j1)).setDirection("{<}");
                }
            }
            for(int i1 = r + 1; i1 < row; ++i1) {
                if(mat.get(i1).get(0) instanceof PathCell) {
                    ((PathCell)mat.get(i1).get(0)).setDirection("{v}");
                }
            }
        }
    }

    public void parkCarAtPosition(Car car, int r, int c) {
        if(!isValidEmptyParkingPlace(r,c)) return;
        if(mat.get(r).get(c) instanceof ParkingCell) {
            ((ParkingCell)mat.get(r).get(c)).setCar(car);
            ((ParkingCell)mat.get(r).get(c)).setParkedTime(LocalTime.now());
            ((ParkingCell)mat.get(r).get(c)).setIsParked("P");
        }
        this.vacancy --;
    }

    public void removeDirections() {
        for(int i = 0; i < row; ++i) {
            for (int j = 0; j < col; ++j) {
                if(mat.get(i).get(j) instanceof PathCell) {
                    ((PathCell)mat.get(i).get(j)).setDirection("");
                }
            }
        }
    }

    public ParkingCell getParkingCellByPosition(int r, int c) {
        return (ParkingCell) mat.get(r).get(c);
    }

    public boolean isRowAndColumnInRange(int r, int c) {
        return ((r >= 0 && r < row) && (c >= 0 && c < col));
    }

    public boolean isValidEmptyParkingPlace(int r, int c) {
        return (isRowAndColumnInRange(r,c) &&
                mat.get(r).get(c).getCellValue().equals("-"));
    }

    public CarLocation getCarLocation(int carParkingNumber) {
        for(int i = 0; i < row; ++i) {
            for (int j = 0; j < col; ++j) {
                Cell cell = mat.get(i).get(j);
                if(cell.getPosition() == carParkingNumber) {
                    return new CarLocation(i,j,floorNo);
                }
            }
        }
        return null;
    }
}
