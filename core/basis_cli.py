import sys
from solver import determine_basis

def parse_input(arg):
    vectors = []
    for v in arg.split(";"):
        vectors.append([int(x) for x in v.split(",")])
    return vectors

def print_matrix_from_columns(columns):
    """
    columns: list of column vectors
    Prints them side by side as a matrix
    """
    if not columns:
        print("[]")
        return

    rows = len(columns[0])
    cols = len(columns)

    for i in range(rows):
        row = []
        for j in range(cols):
            row.append(str(columns[j][i]))
        print("[ " + "  ".join(row) + " ]")

    print()  # blank line after matrix

if __name__ == "__main__":

    # Safety check
    if len(sys.argv) < 2:
        print("No input vectors provided.")
        sys.exit(1)

    raw_input = sys.argv[1]
    vectors = parse_input(raw_input)

    result = determine_basis(vectors)

    print("Subset Basis (original vectors, redundant removed):")
    print_matrix_from_columns(result["subset"])

    print("RREF of Original Matrix:")
    print_matrix_from_columns(result["reduced"])

