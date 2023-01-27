package com.jayaraj.CrazyChat7;

//MARK1 - marked message on click
/*
    linear10.setOnClickListener(v -> {
        int fi = _findPositionOfMsg(_position, _getMsg(_position, "mark"));
        _timer.schedule(new TimerTask() {
            @Override
            public void run() {
                requireActivity().runOnUiThread(() -> {
                    if (fi == -1) {
                        Toast.makeText(getContext(), "Unable to Find Message", Toast.LENGTH_SHORT).show();
                    } else {
                        scrollNum = fi;
                        _scrollto((int) scrollNum);
                    }
                });
            }
        }, 1);
    });
*/