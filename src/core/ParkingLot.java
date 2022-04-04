package core;

import java.time.LocalTime;
import java.util.ArrayList;

// Model Class
public class ParkingLot {

    private final int floorNo;
    private final int rows;
    private final int columns;
    private final ArrayList<ArrayList<Cell>> mat;
    private int vacancy;
    private final int pathWidth;
    private static int parkingSpotNumber = 1;

    protected ParkingLot(int floor, int row, int col, int pathWidth) {
        vacancy = row * col;
        this.floorNo = floor;
        this.rows = row + (((row - 1)/2)+2) * pathWidth;
        this.columns = col + (2 * pathWidth);
        this.mat = new ArrayList<>();
        this.pathWidth = pathWidth;
        formatMatrix();
    }

    public int getVacancy() {
        return vacancy;
    }

    public int getFloorNo() {
        return floorNo;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    protected boolean isParkingFull() {
        return this.vacancy == 0;
    }

    protected int getFirstParkingSpotNumber() {
        for(int i = 0; i < rows; ++i) {
            for (int j = 0; j < columns; ++j) {
                if(this.mat.get(i).get(j).getCellValue().equals("-")) {
                    return ((ParkingCell)mat.get(i).get(j)).getParkingSpotNumber();
                }
            }
        }
        return -1;
    }

    protected CarParkingSpot getNearestParkingSpot(int r, int c) {
        int[][] ans = new int[rows][columns];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                ans[i][j] = Integer.MAX_VALUE;
            }
        }

        // For each cell
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                ans[i][j] = Math.abs(i - r) + Math.abs(j - c);
            }
        }

        int ctr = 0;
        while (true) {
            boolean flag = false;
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    if(ans[i][j] == ctr) {
                        flag = true;
                        if (isValidEmptyParkingPlace(i, j)) {
                            return new CarParkingSpot(i,j);
                        }
                    }
                }
            }
            if(!flag) break;
            ctr++;
        }
        return null;
    }

    public CarParkingSpot getCarNumberParkingSpot(String CarNo) {
        for(int i = 0; i < rows; ++i) {
            for (int j = 0; j < columns; ++j) {
                Car car = null;
                if(this.mat.get(i).get(j) instanceof ParkingCell) {
                    car = ((ParkingCell)this.mat.get(i).get(j)).getCar();
                }
                if(car != null && car.getCarNumber() != null && car.getCarNumber().equals(CarNo)) {
                    return new CarParkingSpot(i,j);
                }
            }
        }
        return null;
    }

    public String getParkingLotMap(boolean isDescriptionNeeded) {
        StringBuilder res = new StringBuilder();
        for(int i = 0; i < rows; ++i)
        {
            for(int j = 0; j < columns; ++j)
            {
                res.append(String.format("%8s ", mat.get(i).get(j).getCellValue()));
            }
            res.append("\n");
        }
        if(isDescriptionNeeded) {
            res.append("\nHere, All Path Cells are represented by 'X'");
            res.append("\nAll Empty Parking Places represented by '-'");
            res.append("\nParking Place with Car Parked is represented by CarNumber");
        }
        return res.toString();
    }

    public String getModifiedParkingLotMap(boolean isDescriptionNeeded) {
        StringBuilder res = new StringBuilder();
        for(int i = 0; i < rows; ++i)
        {
            for(int j = 0; j < columns; ++j)
            {
                if(mat.get(i).get(j).getCellValue().equals("X")) {
                    res.append(String.format("%8s ", mat.get(i).get(j).getCellValue()));
                }
                else
                    res.append(String.format("%8s ", ((ParkingCell) mat.get(i).get(j)).getParkingSpotNumber() + "-" +
                            ((ParkingCell) mat.get(i).get(j)).getIsParked()));
            }
            res.append("\n");
        }
        if(isDescriptionNeeded) {
            res.append("\nHere, All Path Cells are represented by 'X'");
            res.append("\nAll Parking Places represented by [ParkingSpotNumber-E] or [ParkingSpotNumber-P]");
            res.append("\nParkingSpotNumber is the Number to represent the parking place");
            res.append("\nE stands for Empty, P stands for Parked");
        }
        return res.toString();
    }

    protected String getDetailedPath() {
        StringBuilder res = new StringBuilder();
        for(int i = 0; i < rows; ++i)
        {
            for(int j = 0; j < columns; ++j)
            {
                if(mat.get(i).get(j).getCellValue().equals("X")) {
                    res.append(String.format("%8s ", mat.get(i).get(j).getCellValue() +
                            ((PathCell) mat.get(i).get(j)).getDirection()));
                }
                else {
                    res.append(String.format("%8s ", mat.get(i).get(j).getCellValue()));
                }
            }
            res.append("\n");
        }
        res.append("\nHere, Car path represented by X{D}");
        res.append("\nD represents Direction that the car can travel to reach parking place");
        return res.toString();
    }

    public ParkingCell getParkingCellByPosition(int r, int c) {
        return (ParkingCell) mat.get(r).get(c);
    }

    protected CarParkingSpot getCarLocation(int carParkingNumber) {
        for(int i = 0; i < rows; ++i) {
            for (int j = 0; j < columns; ++j) {
                Cell cell = mat.get(i).get(j);
                if(cell instanceof ParkingCell) {
                    if(((ParkingCell)cell).getParkingSpotNumber() == carParkingNumber) {
                        return new CarParkingSpot(i,j);
                    }
                }
            }
        }
        return null;
    }

    protected boolean isRowAndColumnInRange(int r, int c) {
        return ((r >= 0 && r < rows) && (c >= 0 && c < columns));
    }

    protected boolean isValidEmptyParkingPlace(int r, int c) {
        return (isRowAndColumnInRange(r,c) &&
                mat.get(r).get(c).getCellValue().equals("-"));
    }

    protected void setDetailedLeftEntryPath(int r, int c) {
        if(mat.get(r+1).get(c).getCellValue().equals("X")) {
            int j = 0;
            for(int i = rows - 1; i > r+1; --i) {
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
            int i = rows - (pathWidth);
            int j = columns - (pathWidth);
            if(mat.get(rows - 1).get(0) instanceof  PathCell) {
                ((PathCell)mat.get(rows - 1).get(0)).setDirection("{^}");
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

    protected void setDetailedEntryPath(int r, int c) {
        int j = pathWidth - 1;
        if(mat.get(r-1).get(c).getCellValue().equals("X")) {
            for(int i = rows - 1; i >= r; --i) {
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
            int to = columns - (pathWidth);
            for(int i = rows - 1; i >= r - 1; --i) {
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

    protected void setDetailedLeftExitPath(int r, int c) {
        int j = pathWidth - 1;
        int jj = columns - (pathWidth);
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
            for(int i1 = i; i1 < rows; ++i1) {
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
            for(int i1 = r - 1; i1 < rows; ++i1) {
                if(mat.get(i1).get(j) instanceof PathCell) {
                    ((PathCell)mat.get(i1).get(j)).setDirection("{v}");
                }
            }
        }
    }

    protected void setDetailedExitPath(int r, int c) {
        int i = rows - (pathWidth);
        int j = columns - (pathWidth);
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
            for(int i1 = i; i1 < rows; ++i1) {
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
            for(int i1 = r + 1; i1 < rows; ++i1) {
                if(mat.get(i1).get(0) instanceof PathCell) {
                    ((PathCell)mat.get(i1).get(0)).setDirection("{v}");
                }
            }
        }
    }

    protected void parkCarAtPosition(Car car, int r, int c) {
        if(!isValidEmptyParkingPlace(r,c)) return;
        if(mat.get(r).get(c) instanceof ParkingCell) {
            ((ParkingCell)mat.get(r).get(c)).setCar(car);
            ((ParkingCell)mat.get(r).get(c)).setParkedTime(LocalTime.now());
            ((ParkingCell)mat.get(r).get(c)).setIsParked("P");
        }
        this.vacancy --;
    }

    protected ParkingCell exitACar(CarParkingSpot pos) {
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

    protected void removeDirections() {
        for(int i = 0; i < rows; ++i) {
            for (int j = 0; j < columns; ++j) {
                if(mat.get(i).get(j) instanceof PathCell) {
                    ((PathCell)mat.get(i).get(j)).setDirection("");
                }
            }
        }
    }

    private void formatMatrix() {
        if(pathWidth == 1) {
            for (int i = 0; i < rows; ++i)
            {
                ArrayList<Cell> cells = new ArrayList<>();
                for(int j = 0; j < columns; ++j)
                {
                    if(i == rows - 1 || i % 3 == 0 || j == 0 || j == columns - 1) {
                        PathCell pathCell = new PathCell();
                        cells.add(pathCell);
                    }
                    else {
                        ParkingCell parkingCell = new ParkingCell(parkingSpotNumber);
                        parkingSpotNumber++;
                        parkingCell.setIsParked("E");
                        cells.add(parkingCell);
                    }
                }
                this.mat.add(cells);
            }
        }
        else if(pathWidth == 2){
            for (int i = 0; i < rows; ++i)
            {
                ArrayList<Cell> cells = new ArrayList<>();
                for(int j = 0; j < columns; ++j)
                {
                    if(i <= 1 || i >= rows - 2 || i % 4 == 0 || i % 4 == 1 || j <= 1 || j >= columns - 2) {
                        PathCell pathCell = new PathCell();
                        cells.add(pathCell);
                    }
                    else {
                        ParkingCell parkingCell = new ParkingCell(parkingSpotNumber);
                        parkingSpotNumber++;
                        parkingCell.setIsParked("E");
                        cells.add(parkingCell);
                    }
                }
                this.mat.add(cells);
            }
        }
    }
}