#include <stdio.h>

// Function prototypes
void print_board(char board[3][3]);
int check_winner(char board[3][3], char player);
int is_board_full(char board[3][3]);
void play_game();

int main(void)
{
    play_game();
    return 0;
}

// Prints the Tic-Tac-Toe board
void print_board(char board[3][3])
{
    printf("\n");
    for (int i = 0; i < 3; i++)
    {
        printf(" %c | %c | %c \n", board[i][0], board[i][1], board[i][2]);
        if (i < 2)
            printf("---|---|---\n");
    }
    printf("\n");
}

// Checks if the specified player has won
int check_winner(char board[3][3], char player)
{
    // Check rows
    for (int i = 0; i < 3; i++)
    {
        if (board[i][0] == player && board[i][1] == player && board[i][2] == player)
            return 1;
    }
    // Check columns
    for (int i = 0; i < 3; i++)
    {
        if (board[0][i] == player && board[1][i] == player && board[2][i] == player)
            return 1;
    }
    // Check diagonals
    if (board[0][0] == player && board[1][1] == player && board[2][2] == player)
        return 1;
    if (board[0][2] == player && board[1][1] == player && board[2][0] == player)
        return 1;
    return 0;
}

// Checks if the board is full (draw)
int is_board_full(char board[3][3])
{
    for (int i = 0; i < 3; i++)
    {
        for (int j = 0; j < 3; j++)
        {
            if (board[i][j] == ' ')
                return 0;
        }
    }
    return 1;
}

// Main game logic
void play_game()
{
    char board[3][3] = {
        {' ', ' ', ' '},
        {' ', ' ', ' '},
        {' ', ' ', ' '}
    };
    char current_player = 'X';
    int row, col;

    printf("Welcome to Tic-Tac-Toe!\n");
    printf("Enter row (0-2) and column (0-2) to make a move.\n");

    while (1)
    {
        print_board(board);
        printf("Player %c's turn. Enter row and column: ", current_player);
        scanf("%d %d", &row, &col);

        // Validate input
        if (row < 0 || row > 2 || col < 0 || col > 2)
        {
            printf("Invalid move! Row and column must be between 0 and 2.\n");
            continue;
        }
        if (board[row][col] != ' ')
        {
            printf("Cell already taken! Try again.\n");
            continue;
        }

        // Make move
        board[row][col] = current_player;

        // Check for winner
        if (check_winner(board, current_player))
        {
            print_board(board);
            printf("Player %c wins!\n", current_player);
            break;
        }

        // Check for draw
        if (is_board_full(board))
        {
            print_board(board);
            printf("It's a draw!\n");
            break;
        }

        // Switch player
        current_player = (current_player == 'X') ? 'O' : 'X';
    }
}