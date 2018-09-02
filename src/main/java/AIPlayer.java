import com.sun.org.glassfish.gmbal.GmbalException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class AIPlayer extends   Player {


    double[] weights;

    public AIPlayer(double[] weights){
      super();
      this.weights = weights;
    }

    public double hypothesis(int[] features){
      return sigmoid(dotProduct(features,weights));
    }


    public double dotProduct(int[] features, double[] weights){
      int length = features.length;
      double result = 0;
      for(int i = 0; i < length; i++) result += features[i]*weights[i];

      return result;
    }

    public double sigmoid(double x){
      return 1 / (1 + Math.exp(-x));
    }

    public double evaluateMove(GameState gameState){
      int[] features = new int[10];
      features[0] = 1;
      int index = 0;
      for(int i = 0; i<3;i++){
        for(int j = 0; j<3; j++){
          index++;
          features[index] = gameState.getValueAt(i,j);
        }
      }
      return hypothesis(features);
    }

    @Override
    public GameState play(GameState gameState){
      List<GameState> possibleMoves = getPossibleMoves(gameState);
      return getTheBestMove(possibleMoves);
    }

    public List<GameState> getPossibleMoves(GameState gameState){
      List<GameState> result = new ArrayList<>();
      for (int i = 0; i<= 2; i++){
        for(int j = 0; j <= 2; j++){
          if(gameState.moveIsValid(i,j)){
            int[][] possibleMap = cloneGameMap(gameState.getGameMap());
            if(this.playerSymbol == 'X') possibleMap[i][j] = 1;
            else possibleMap[i][j] = -1;
            GameState possibleGameState = new GameState(possibleMap);
            result.add(possibleGameState);
          }
        }
      }
      return result;
    }

    public int[][] cloneGameMap(int[][] originalMap){
      int[][] result = new int[3][3];
      for (int i = 0; i<= 2; i++){
        for(int j = 0; j <= 2; j++){
          result[i][j] = originalMap[i][j];
        }
      }
      return result;
    }


    public GameState getTheBestMove(List<GameState> possibleMoves){
      double[] stateValues = possibleMoves.stream().mapToDouble(state -> evaluateMove(state)).toArray();
      System.out.println("These are the values");
      Arrays.stream(stateValues).forEach(num -> System.out.println(num));
      GameState gameState = possibleMoves.stream().max(Comparator.comparing(state -> evaluateMove(state))).get();
      System.out.println("We have chosen this values:  " + evaluateMove(gameState));
      return gameState;

    }

}