import java.security.SecureRandom;
public class knightTour {
	
	private int[][] board;//instance variable to simulate chess board
	private int[] horizontal= {2,1,-1,-2,-2,-1,1,2};//instance variable to track horizontal moves
	private int[] vertical= {-1,-2,-2,-1,1,2,2,1};//instance variable to track vertical moves
	private int moveNumber;//instance variable to hold move number
	private int move_counter;//instance variable to count the number of moves made
	private int current_row, current_col;//instance variables to track the piece's dimensions
	private int prev_row, prev_col;//instance variables to track the piece's previous dimensions
	private SecureRandom rand_move;//object to randomize piece movement
	private int[] vert_corner= {0, 7, 0, 7};
	private int[] hor_corner= {0, 0, 7, 7};
	private int[][] acc_heur;
	
	public knightTour() {
		this.board= new int[8][8];
		this.acc_heur= new int[8][8];
		this.initializeBoard();
		//this.selectCorner();
		this.rand_move= new SecureRandom();
		this.move_counter=0;
		this.initAccHeu();
		
	}
	
	//method to randomly select a corner to start from
	private void selectCorner() {
		SecureRandom rand_corner= new SecureRandom();
		int val=rand_corner.nextInt(4);
		//System.out.println("val: "+val);
		this.selectSquare(this.hor_corner[val], this.vert_corner[val]);
		this.setPrevDime(this.getCurrentRow(), this.getCurrentCol());
		this.initializeCurrentDimensions(this.hor_corner[val], this.vert_corner[val]);
		System.out.println("row: "+this.getCurrentRow()+" col: "+this.getCurrentCol());
	}
	
	private boolean isLegal(int hor_corn, int vert_corn, int hor, int vert) {
		int diff= Math.abs((hor+vert)-(hor_corn+vert_corn));
		if((diff == 3 || diff == 1))
			return true;
		return false;
	}
	
	//finding squares accessible to any of the 4 corners
	private void selectSquare(int hor, int vert) {
		int[] square_x= new int[2];
		int[] square_y= new int[2];
		
		SecureRandom rand_square= new SecureRandom();
		int x=0;
		int val1= rand_square.nextInt(2);
		for(int i=0; i<8; i++) {
			if(!this.isOut((hor+this.horizontal[i]),(vert+this.vertical[i])) && this.isLegal(
					hor,vert, (hor+this.horizontal[i]), (vert+this.vertical[i]))){
				square_x[x]=(hor+this.horizontal[i]);
				square_y[x]=(vert+this.vertical[i]);
				//System.out.println("hor: "+(hor+this.horizontal[i])+" vert: "+(vert+this.vertical[i]));
				//System.out.println("Square found!");
				x++;
			}
			
			if(x==2)
			{	
				break;
			}
		}
		this.initializeCurrentDimensions(square_x[val1], square_y[val1]);
		//System.out.println("rowx: "+square_x[val1]+" coly: "+square_y[val1]);
	}
	
	//method to find the accessibility number of each square
	private int getNumSquare(int hor, int vert) {
		int counter=0;
		for(int i=0; i<8; i++) {
			if(!this.isOut((hor+this.horizontal[i]),(vert+this.vertical[i])) && this.isLegal(
					hor,vert, (hor+this.horizontal[i]), (vert+this.vertical[i]))){
				counter++;
			}
		}
		return counter;
	}
	
	//method to find the square with the lowest accessibility number
	private void findLowestAccessibility(int hor, int vert) {
		int lowest=8;
		for(int i=0; i<8; i++) {
			if(!this.isOut((hor+this.horizontal[i]),(vert+this.vertical[i])) && this.isLegal(
					hor,vert, (hor+this.horizontal[i]), (vert+this.vertical[i]))){
				if(lowest>=this.acc_heur[(hor+this.horizontal[i])][(vert+this.vertical[i])]) {
					if(!this.isVisited((hor+this.horizontal[i]), (vert+this.vertical[i]))) {
						lowest=this.acc_heur[(hor+this.horizontal[i])][(vert+this.vertical[i])];
						this.initializeCurrentDimensions(
								(hor+this.horizontal[i]),(vert+this.vertical[i]));
						//System.out.println("Found");
						//System.out.println("Shor "+(hor+this.horizontal[i])+" Svert: "+(vert+this.vertical[i]));
					}
				}
			}
			//System.out.println("R: "+hor+" C: "+vert);
			//System.out.println("hor "+this.getCurrentRow()+" vert: "+this.getCurrentCol());
		}
		this.setPrevDime(hor, vert);
	}
	
	//method to initialize the accessibility heuristic
	private void initAccHeu() {
		for(int row=0; row<8; row++)
			for(int col=0; col<8; col++)
				this.acc_heur[row][col]= this.getNumSquare(row, col);
		
		for(int i=0; i<8; i++)
			for(int j=0; j<8; j++)
			{
				System.out.printf("%3d",this.acc_heur[i][j]);
				if(j==7)
					System.out.println();
			}
		System.out.println();
	}
	
	//method to initialize each square on the board to zero
	private void initializeBoard() {
		for(int row=0; row<8; row++)
			for(int col=0; col<8; col++)
				this.board[row][col]=0;
	}
	
	//method to simulate knight movement on the chessboard
	public void moveKnight() {
		while(this.move_counter != 64) {
			if(this.move_counter != 0)
			{
				/*this.moveNumber= this.rand_move.nextInt(8);
				this.setPrevDime(this.getCurrentRow(), this.getCurrentCol());
				this.setMoveDimensions(this.horizontal[moveNumber], this.vertical[moveNumber]);*/
				this.findLowestAccessibility(this.getCurrentRow(), this.getCurrentCol());
			}
			if(this.moveExist()) {
				move_counter++;
				this.board[this.getCurrentRow()][this.getCurrentCol()]= move_counter;
			}
			else
				break;
			
			/*if(!this.isOut(this.getCurrentRow(), this.getCurrentCol()) && this.isLegal(
					this.getCurrentRow(),this.getCurrentCol(), this.getPrevRow(), this.getPrevCol()))
			{
				if(!this.isVisited(this.getCurrentRow(), this.getCurrentCol())) {
					move_counter++;
					this.board[this.getCurrentRow()][this.getCurrentCol()]= move_counter;
				}
				else
				{	
					System.out.println("Restored");
					this.reStore(this.getPrevRow(), this.getPrevCol());
					System.out.printf("PreviousR: %d PreviousC: %d%n", this.getPrevRow(),
							this.getPrevCol());
					if(!this.moveExist()) {
						System.out.println("Move(s) made in tour	: "+move_counter);
						break;
					}
				}
			}
			else
			{	
				System.out.println("move out or illegal");
				this.reStore(this.getPrevRow(), this.getPrevCol());
			}*/
		}
		System.out.println("Move(s) made in tour	: "+this.move_counter);
	}
	
	//method to start tour from all the 64 squares
	public void square64() {
		int count_com=0;
		for(int i=0; i<8; i++)
			for(int j=0; j<8; j++) {
				this.initializeCurrentDimensions(i, j);
				this.moveKnight();
				this.DisplayBoard();
				this.initializeBoard();
				if(this.move_counter==64)
					count_com++;
				this.move_counter=0;
			}
		System.out.printf("%d have been completed%n",count_com);
	}
	
	//method to display the tour of the knight on the chessboard
	public void DisplayBoard() {
		for(int row=0; row<8; row++)
			for(int col=0; col<8; col++) {
				System.out.printf("%-4d",this.board[row][col]);
				if(col == 7)
					System.out.println();
			}
	}
	
	//method to test if the piece is moving out of bounds or not
	private boolean isOut(int row, int col) {
		if(row > 7 || row < 0)
			return true;
		if(col > 7 || col < 0)
			return true;
		return false;
	}
	
	//returns the value of the current row
	private int getCurrentRow() {
		return this.current_row;
	}
	
	//returns the value of the current column
	private int getCurrentCol() {
		return this.current_col;
	}
	
	//returns the value of the previous row
	private int getPrevRow() {
		return this.prev_row;
	}
	
	//returns the value of the previous column
	private int getPrevCol() {
		return this.prev_col;
	}
	
	//method to test if square is visited or not
	private boolean isVisited(int row, int col) {
		if(this.board[row][col] != 0)
			return true;
		return false;
	}
	
	//method restores position
	private void reStore(int hor, int vert) {
		/*if(hor > 0)
			this.current_row-=hor;
		else
			this.current_row+=(-1*hor);
		
		if(vert > 0)
			this.current_col-=vert;
		else
			this.current_col+=(-1*vert);*/
		this.initializeCurrentDimensions(hor, vert);
	}
	
	//method to set potential square move
	private void setMoveDimensions(int hor, int vert) {
		this.current_row+= this.horizontal[moveNumber];
		this.current_col+= this.vertical[moveNumber];
	}
	
	private void initializeCurrentDimensions(int hor, int vert) {
		this.current_row=hor;
		this.current_col=vert;
	}
	
	//method to set the previous square dimensions
	private void setPrevDime(int row , int col) {
		this.prev_row=row;
		this.prev_col=col;
	}
	
	//method to detect if there are moves or not
	private boolean moveExist() {
		int row_test= this.getPrevRow(), col_test= this.getPrevCol();
		int count_moves=0;
		
		for(int mov=0; mov<8; mov++) {
			row_test+=this.horizontal[mov];
			col_test+=this.vertical[mov];
			
			if(!this.isOut(row_test, col_test))
				if(!isVisited(row_test, col_test)) {//not visited indicated there exists a move(s)
					++count_moves;
				}
			row_test= this.getPrevRow();
			col_test= this.getPrevCol();
		}
		
		if(count_moves>0)
			return true;
		return false;
	}
	
	
}
