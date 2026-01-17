import sympy as sp

def determine_basis(vectors):
    """
    Returns:
    - subset basis (original vectors, redundant removed)
    - reduced basis of the SAME column space
    """
    if not vectors:
        return {"subset": [], "reduced": []}

    # Treat input vectors as COLUMNS
    col_matrix = sp.Matrix(vectors).T

    # ---------- Subset Basis (from original vectors) ----------
    subset_basis = col_matrix.columnspace()
    subset_result = [[int(x) for x in vec] for vec in subset_basis]

    # ---------- Reduced Basis (COLUMN SPACE) ----------
    # RREF of column-space matrix
    rref_matrix, _ = col_matrix.rref()

    reduced_result = []
    for col in rref_matrix.T.tolist():
        if any(val != 0 for val in col):
            reduced_result.append([int(val) for val in col])

    return {
        "subset": subset_result,
        "reduced": reduced_result
    }

