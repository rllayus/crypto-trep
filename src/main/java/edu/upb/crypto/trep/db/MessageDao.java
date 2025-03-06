package edu.upb.crypto.trep.db;

import edu.upb.crypto.trep.StringUtil;

import java.net.ConnectException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


public class MessageDao {

    private DaoHelper<Message> helper;
    public MessageDao(){
        helper = new DaoHelper<>();
    }

    DaoHelper.ResultReader<Message> resultReader = result -> {
        Message message = new Message();
        if (existColumn(result, Message.Column.ID)) {
            message.setId(result.getLong(Contact.Column.ID));
        }
        if (existColumn(result, Message.Column.codeMessage))
            message.setCodeMessage(result.getString(Message.Column.codeMessage));
        if (existColumn(result, Message.Column.senderCode))
            message.setSenderCode(StringUtil.trim(result.getString(Message.Column.senderCode)));
        if (existColumn(result, Message.Column.recipientCode))
            message.setRecipientCode(StringUtil.trim(result.getString(Message.Column.recipientCode)));
        if (existColumn(result, Message.Column.message))
            message.setMessage(StringUtil.trim(result.getString(Message.Column.message)));
        if (existColumn(result, Message.Column.createdDate))
            message.setDate(result.getDate(Message.Column.createdDate));
        if (existColumn(result, Message.Column.type))
            message.setType(result.getString(Message.Column.type));
        if (existColumn(result, Message.Column.senderName))
            message.setSenderName(result.getString(Message.Column.senderName));
        return message;
    };

    public static boolean existColumn(ResultSet result, String columnName) {
        try {
            result.findColumn(columnName);
            return true;
        } catch (SQLException sqlex) {
            //log.error("No se encontro la columna: {}", columnName); // log innecesario
        }
        return false;
    }

    public List<Message> findAll() throws ConnectException, SQLException {
        String query = "SELECT * FROM message m INNER JOIN contact c on m.sender_code =c.code ";
        return helper.executeQuery(query,  resultReader);
    }
    public List<Message> findAll(String roomCode) throws ConnectException, SQLException {
        String query = "SELECT m.*, c.name FROM message m INNER JOIN contact c on m.sender_code = c.code where room_code=? ORDER BY m.id asc";
        return helper.executeQuery(query, pst -> pst.setString(1, roomCode), resultReader);
    }

    public boolean exist(String argument) throws ConnectException, SQLException {
        String query = "SELECT count(*) FROM message WHERE "+ argument;
        return helper.executeQueryCount(query, null) == 1;
    }

    public void update(String query) throws Exception {
        helper.update(query, null);
    }
    public void save(Message message) throws Exception {
        String query = "INSERT INTO message(cod_message, sender_code, recipient_code, created_date, message, type, room_code) values (?,?,?,?,?,?,?)";
        DaoHelper.QueryParameters params = new DaoHelper.QueryParameters() {
            @Override
            public void setParameters(PreparedStatement pst) throws SQLException {
                pst.setString(1,message.getCodeMessage());
                pst.setString(2,message.getSenderCode());
                pst.setString(3,message.getRecipientCode());
                pst.setDate(4, new Date(System.currentTimeMillis()));
                pst.setString(5,message.getMessage());
                pst.setString(6,message.getType());
                pst.setString(7,message.getRoomCode());
            }
        };
         helper.insert(query, params, message);
    }

    public void update(String query, String conditionWhere) throws SQLException, ConnectException {
        if (query.trim().endsWith("%s")) {
            query = String.format(query, conditionWhere);
        } else {
            query = String.format("%s %s", query, conditionWhere);
        }
        helper.update(query, null);
    }
}
