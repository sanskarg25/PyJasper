import os
import pytest
from py4jasper.main import generate_report

def test_generate_report(monkeypatch):
    def mock_generate_report(*args, **kwargs):
        return None  # ? Simulate successful Java interaction

    monkeypatch.setattr("py4jasper.main.JavaGateway", lambda: mock_generate_report)

    file_type = "pdf"
    input_file_path = "example_input.jrxml"
    output = generate_report(file_type, input_file_path)

    expected_output = os.path.join(
        os.getcwd(), f"{os.path.splitext(os.path.basename(input_file_path))[0]}.{file_type}"
    )
    assert output == expected_output
