 let data = {
    "nodes": [
      {
        "name": "开始节点",
        "size": [
          "50",
          "35"
        ],
        "type": "node",
        "x": 128,
        "y": 57,
        "shape": "customNode",
        "color": "#1890ff",
        "image": "data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0idXRmLTgiPz4KPCEtLSBHZW5lcmF0b3I6IEFkb2JlIElsbHVzdHJhdG9yIDIxLjAuMCwgU1ZHIEV4cG9ydCBQbHVnLUluIC4gU1ZHIFZlcnNpb246IDYuMDAgQnVpbGQgMCkgIC0tPgo8c3ZnIHZlcnNpb249IjEuMSIgaWQ9IuWbvuWxgl8xIiB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHhtbG5zOnhsaW5rPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5L3hsaW5rIiB4PSIwcHgiIHk9IjBweCIKCSB2aWV3Qm94PSIwIDAgNjggNjgiIHN0eWxlPSJlbmFibGUtYmFja2dyb3VuZDpuZXcgMCAwIDY4IDY4OyIgeG1sOnNwYWNlPSJwcmVzZXJ2ZSI+CjxzdHlsZSB0eXBlPSJ0ZXh0L2NzcyI+Cgkuc3Qwe2ZpbGw6I0ZDRjFFODt9Cgkuc3Qxe2ZpbGw6I0Y5QUE2ODt9Cgkuc3Qye2ZpbGw6I0ZBOEMxNjt9Cjwvc3R5bGU+Cjx0aXRsZT5zdGFydF8xPC90aXRsZT4KPGcgaWQ9IuWbvuWxgl8yIj4KCTxnIGlkPSLlm77lsYJfMS0yIj4KCQk8cGF0aCBjbGFzcz0ic3QwIiBkPSJNMzQsNjcuNWMtMTguNSwwLTMzLjUtMTUtMzMuNS0zMy41UzE1LjUsMC41LDM0LDAuNXMzMy41LDE1LDMzLjUsMzMuNUM2Ny41LDUyLjUsNTIuNSw2Ny41LDM0LDY3LjV6Ii8+CgkJPHBhdGggY2xhc3M9InN0MSIgZD0iTTM0LDFjMTguMiwwLDMzLDE0LjgsMzMsMzNTNTIuMiw2NywzNCw2N1MxLDUyLjIsMSwzNFMxNS44LDEsMzQsMSBNMzQsMEMxNS4yLDAsMCwxNS4yLDAsMzRzMTUuMiwzNCwzNCwzNAoJCQlzMzQtMTUuMiwzNC0zNFM1Mi44LDAsMzQsMHoiLz4KCTwvZz4KCTxwb2x5Z29uIGNsYXNzPSJzdDIiIHBvaW50cz0iNDguMiwzMi42IDI4LjIsMTcuNSAyOC4yLDQ3LjggCSIvPgo8L2c+Cjwvc3ZnPgo=",
        "imageWidth": 20,
        "imageHeight": 20,
        "inPoints": [
          [
            0,
            0.5
          ]
        ],
        "outPoints": [
          [
            1,
            0.5
          ]
        ],
        "dragItemConfig": {
          "image": "data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0idXRmLTgiPz4KPCEtLSBHZW5lcmF0b3I6IEFkb2JlIElsbHVzdHJhdG9yIDIxLjAuMCwgU1ZHIEV4cG9ydCBQbHVnLUluIC4gU1ZHIFZlcnNpb246IDYuMDAgQnVpbGQgMCkgIC0tPgo8c3ZnIHZlcnNpb249IjEuMSIgaWQ9IuWbvuWxgl8xIiB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHhtbG5zOnhsaW5rPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5L3hsaW5rIiB4PSIwcHgiIHk9IjBweCIKCSB2aWV3Qm94PSIwIDAgNjggNjgiIHN0eWxlPSJlbmFibGUtYmFja2dyb3VuZDpuZXcgMCAwIDY4IDY4OyIgeG1sOnNwYWNlPSJwcmVzZXJ2ZSI+CjxzdHlsZSB0eXBlPSJ0ZXh0L2NzcyI+Cgkuc3Qwe2ZpbGw6I0ZDRjFFODt9Cgkuc3Qxe2ZpbGw6I0Y5QUE2ODt9Cgkuc3Qye2ZpbGw6I0ZBOEMxNjt9Cjwvc3R5bGU+Cjx0aXRsZT5zdGFydF8xPC90aXRsZT4KPGcgaWQ9IuWbvuWxgl8yIj4KCTxnIGlkPSLlm77lsYJfMS0yIj4KCQk8cGF0aCBjbGFzcz0ic3QwIiBkPSJNMzQsNjcuNWMtMTguNSwwLTMzLjUtMTUtMzMuNS0zMy41UzE1LjUsMC41LDM0LDAuNXMzMy41LDE1LDMzLjUsMzMuNUM2Ny41LDUyLjUsNTIuNSw2Ny41LDM0LDY3LjV6Ii8+CgkJPHBhdGggY2xhc3M9InN0MSIgZD0iTTM0LDFjMTguMiwwLDMzLDE0LjgsMzMsMzNTNTIuMiw2NywzNCw2N1MxLDUyLjIsMSwzNFMxNS44LDEsMzQsMSBNMzQsMEMxNS4yLDAsMCwxNS4yLDAsMzRzMTUuMiwzNCwzNCwzNAoJCQlzMzQtMTUuMiwzNC0zNFM1Mi44LDAsMzQsMHoiLz4KCTwvZz4KCTxwb2x5Z29uIGNsYXNzPSJzdDIiIHBvaW50cz0iNDguMiwzMi42IDI4LjIsMTcuNSAyOC4yLDQ3LjggCSIvPgo8L2c+Cjwvc3ZnPgo=",
          "width": 42,
          "height": 42
        },
        "attrs": {
          "type": "startevent-none",
          "name": "start"
        },
        "offsetX": 15,
        "offsetY": 27,
        "id": "node2"
      },
      {
        "name": "任务节点",
        "label": "task8",
        "size": [
          "170",
          "50"
        ],
        "type": "node",
        "x": 147,
        "y": 203,
        "shape": "customNode",
        "color": "#1890ff",
        "inPoints": [
          [
            0,
            0.5
          ]
        ],
        "outPoints": [
          [
            1,
            0.5
          ]
        ],
        "image": "data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0idXRmLTgiPz4KPCEtLSBHZW5lcmF0b3I6IEFkb2JlIElsbHVzdHJhdG9yIDIxLjAuMCwgU1ZHIEV4cG9ydCBQbHVnLUluIC4gU1ZHIFZlcnNpb246IDYuMDAgQnVpbGQgMCkgIC0tPgo8c3ZnIHZlcnNpb249IjEuMSIgaWQ9IuWbvuWxgl8xIiB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHhtbG5zOnhsaW5rPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5L3hsaW5rIiB4PSIwcHgiIHk9IjBweCIKCSB2aWV3Qm94PSIwIDAgMTA3IDYxIiBzdHlsZT0iZW5hYmxlLWJhY2tncm91bmQ6bmV3IDAgMCAxMDcgNjE7IiB4bWw6c3BhY2U9InByZXNlcnZlIj4KPHN0eWxlIHR5cGU9InRleHQvY3NzIj4KCS5zdDB7ZmlsbDojRTZGMkZDO3N0cm9rZTojNUNBRkZCO3N0cm9rZS1taXRlcmxpbWl0OjEwO30KCS5zdDF7ZmlsbDojMTg5MEZGO30KPC9zdHlsZT4KPHRpdGxlPnRhc2tfMTwvdGl0bGU+CjxnIGlkPSLlm77lsYJfMiI+Cgk8ZyBpZD0i5Zu+5bGCXzEtMiI+CgkJPHBhdGggY2xhc3M9InN0MCIgZD0iTTguOSwwLjVoODkuMmM0LjYsMCw4LjQsMy44LDguNCw4LjR2NDMuMmMwLDQuNi0zLjgsOC40LTguNCw4LjRIOC45Yy00LjYsMC04LjQtMy44LTguNC04LjRWOC45CgkJCUMwLjUsNC4zLDQuMywwLjUsOC45LDAuNXoiLz4KCTwvZz4KPC9nPgo8cGF0aCBjbGFzcz0ic3QxIiBkPSJNNDcuMywyMS41YzAtMy4zLDAuMy01LjgsMy41LTcuNGMwLjctMC4zLDIuMi0wLjUsMi44LTAuNWMwLjgsMCwwLTAuNywxLjgtMC43czQuMSwwLjgsNS4zLDMuMXMxLjMsNSwxLjMsNS42CgljMCwwLjcsMC43LDAuMywwLjcsMS4zYzAsMS0wLjMsMi44LTEuMyw0Yy0xLDEuMy0xLjMsMS41LTIsMi41Yy0wLjcsMS0xLDItMSwyLjhjMCwzLjEsNS4zLDMuMyw4LjQsNS4zYzEuMiwwLjgsMiwxLjgsMi41LDMKCWMwLjUsMS4yLTAuMiwyLjMtMS4yLDIuNmMtMC4yLDAuMi0wLjUsMC4yLTAuOCwwLjJINDEuOWMtMS4yLDAtMi4yLTEtMi4yLTIuMmMwLTAuMywwLTAuNSwwLjItMC44YzAuNS0xLjIsMS4zLTIuMiwyLjUtMwoJYzMuMS0yLDguNC0yLjIsOC40LTUuM2MwLTAuOC0wLjMtMS44LTEtMi44cy0xLTEuMy0yLTIuNWMtMS0xLjMtMS4zLTMuMS0xLjMtNEM0Ni41LDIyLDQ3LjMsMjIuMiw0Ny4zLDIxLjV6Ii8+Cjwvc3ZnPgo=",
        "dragItemConfig": {
          "image": "data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0idXRmLTgiPz4KPCEtLSBHZW5lcmF0b3I6IEFkb2JlIElsbHVzdHJhdG9yIDIxLjAuMCwgU1ZHIEV4cG9ydCBQbHVnLUluIC4gU1ZHIFZlcnNpb246IDYuMDAgQnVpbGQgMCkgIC0tPgo8c3ZnIHZlcnNpb249IjEuMSIgaWQ9IuWbvuWxgl8xIiB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHhtbG5zOnhsaW5rPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5L3hsaW5rIiB4PSIwcHgiIHk9IjBweCIKCSB2aWV3Qm94PSIwIDAgMTA3IDYxIiBzdHlsZT0iZW5hYmxlLWJhY2tncm91bmQ6bmV3IDAgMCAxMDcgNjE7IiB4bWw6c3BhY2U9InByZXNlcnZlIj4KPHN0eWxlIHR5cGU9InRleHQvY3NzIj4KCS5zdDB7ZmlsbDojRTZGMkZDO3N0cm9rZTojNUNBRkZCO3N0cm9rZS1taXRlcmxpbWl0OjEwO30KCS5zdDF7ZmlsbDojMTg5MEZGO30KPC9zdHlsZT4KPHRpdGxlPnRhc2tfMTwvdGl0bGU+CjxnIGlkPSLlm77lsYJfMiI+Cgk8ZyBpZD0i5Zu+5bGCXzEtMiI+CgkJPHBhdGggY2xhc3M9InN0MCIgZD0iTTguOSwwLjVoODkuMmM0LjYsMCw4LjQsMy44LDguNCw4LjR2NDMuMmMwLDQuNi0zLjgsOC40LTguNCw4LjRIOC45Yy00LjYsMC04LjQtMy44LTguNC04LjRWOC45CgkJCUMwLjUsNC4zLDQuMywwLjUsOC45LDAuNXoiLz4KCTwvZz4KPC9nPgo8cGF0aCBjbGFzcz0ic3QxIiBkPSJNNDcuMywyMS41YzAtMy4zLDAuMy01LjgsMy41LTcuNGMwLjctMC4zLDIuMi0wLjUsMi44LTAuNWMwLjgsMCwwLTAuNywxLjgtMC43czQuMSwwLjgsNS4zLDMuMXMxLjMsNSwxLjMsNS42CgljMCwwLjcsMC43LDAuMywwLjcsMS4zYzAsMS0wLjMsMi44LTEuMyw0Yy0xLDEuMy0xLjMsMS41LTIsMi41Yy0wLjcsMS0xLDItMSwyLjhjMCwzLjEsNS4zLDMuMyw4LjQsNS4zYzEuMiwwLjgsMiwxLjgsMi41LDMKCWMwLjUsMS4yLTAuMiwyLjMtMS4yLDIuNmMtMC4yLDAuMi0wLjUsMC4yLTAuOCwwLjJINDEuOWMtMS4yLDAtMi4yLTEtMi4yLTIuMmMwLTAuMywwLTAuNSwwLjItMC44YzAuNS0xLjIsMS4zLTIuMiwyLjUtMwoJYzMuMS0yLDguNC0yLjIsOC40LTUuM2MwLTAuOC0wLjMtMS44LTEtMi44cy0xLTEuMy0yLTIuNWMtMS0xLjMtMS4zLTMuMS0xLjMtNEM0Ni41LDIyLDQ3LjMsMjIuMiw0Ny4zLDIxLjV6Ii8+Cjwvc3ZnPgo=",
          "width": 80,
          "height": 44
        },
        "attrs": {
          "type": "task",
          "name": "task8",
          "displayName": "fddfd",
          "btns-checkedKeys": [
            "1",
            "3"
          ],
          "enableAttachment": "0",
          "useRole": "1",
          "actors-checkedKeys": [],
          "roles-checkedKeys": []
        },
        "offsetX": 65,
        "offsetY": 18,
        "displayName": "task8",
        "id": "node9"
      }
    ],
    "edges": [
      {
        "id": "edge66",
        "source": "node2",
        "target": "node9",
        "start": {
          "x": 0,
          "y": 17
        },
        "end": {
          "x": 0,
          "y": -25
        },
        "shape": "customEdge",
        "type": "edge",
        "startPoint": {
          "x": 130.34246575342465,
          "y": 75
        },
        "endPoint": {
          "x": 143.68150684931507,
          "y": 177.5
        }
      }
    ],
    "groups": []
  }

  export default data
