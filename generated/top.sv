// Generated by CIRCT firtool-1.62.0
module IntegerReservationStation(	// src/main/scala/sourceNode/rs.scala:21:7
  output       io_redirect_valid,	// src/main/scala/sourceNode/rs.scala:24:14
  output [1:0] io_redirect_bits	// src/main/scala/sourceNode/rs.scala:24:14
);

  assign io_redirect_valid = 1'h0;	// src/main/scala/sourceNode/rs.scala:21:7, :28:15
  assign io_redirect_bits = 2'h0;	// rocket-chip/src/main/scala/diplomacy/Nodes.scala:1208:7, src/main/scala/sourceNode/rs.scala:21:7
endmodule

module ExecuteBlock(	// src/main/scala/sourceNode/executeblk.scala:41:7
  output io_s	// src/main/scala/sourceNode/executeblk.scala:42:14
);

  IntegerReservationStation integerReservationStation (	// src/main/scala/sourceNode/executeblk.scala:14:72
    .io_redirect_valid (/* unused */),
    .io_redirect_bits  (/* unused */)
  );
  IntegerReservationStation integerReservationStation1 (	// src/main/scala/sourceNode/executeblk.scala:15:73
    .io_redirect_valid (/* unused */),
    .io_redirect_bits  (/* unused */)
  );
  assign io_s = 1'h0;	// src/main/scala/sourceNode/executeblk.scala:41:7, :46:8
endmodule

module top(	// src/main/scala/top.scala:10:7
  input clock,	// src/main/scala/top.scala:10:7
        reset	// src/main/scala/top.scala:10:7
);

  ExecuteBlock l_soc (	// src/main/scala/top.scala:14:19
    .io_s (/* unused */)
  );
endmodule

