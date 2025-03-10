"""
Module for interacting with the Jasper Java system to generate reports.
"""

import os
from py4j.java_gateway import JavaGateway


def generate_report(file_type: str, input_file_path: str, output_file_path: str, data: dict) -> str:
    """
    Generates a report using the py4j Java Gateway.

    Args:
        file_type (str): The output file format (pdf, docx, xlsx).
        input_file_path (str): The path to the input file.

    Returns:
        str: The output file name.
    """
    # ? Start the Py4J gateway to communicate with Java
    gateway = JavaGateway()
    file_type = 'pdf'
    input_file_path = 'example.jrxml'
    output_file_path = os.path.join(
            os.getcwd(), f"{os.path.splitext(os.path.basename(input_file_path))[0]}.{file_type}"
        )

    data = {
        'name' : 'Test',
        'age' : 20,
        'city' : 'Hyderabad'
    }

    # ? Storing generated file in current directory
    if not output_file_path:
        output_file_path = os.path.join(
            os.getcwd(), f"{os.path.splitext(os.path.basename(input_file_path))[0]}.{file_type}"
        )

    try:
        gateway.entry_point.generate_report(input_file_path, output_file_path, file_type)
    except Exception as e:
        raise RuntimeError(f"Failed to generate report: {e}")

    return output_file_path
