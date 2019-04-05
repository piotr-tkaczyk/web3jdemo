pragma solidity ^0.5.0;

contract SimpleStorage {
    uint public storedData;

    constructor(uint initialValue) public {
        storedData = initialValue;
    }

    function set(uint x) public {
        storedData = x + 1;
    }

    function get() public view returns (uint retVal) {
        return storedData;
    }

}