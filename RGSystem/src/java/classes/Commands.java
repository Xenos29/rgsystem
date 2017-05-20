package classes;

import classes.entity.Log;
import classes.entity.Report;
import classes.entity.User;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;



public class Commands {

    private static SessionFactory factory;

    static {
        Configuration configuration = new Configuration();
        configuration.configure();
        ServiceRegistry serviceRegistry
                = new StandardServiceRegistryBuilder().applySettings(
                        configuration.getProperties()).build();
        factory = configuration.buildSessionFactory(serviceRegistry);

    }

    public static Session getSession() {
        return factory.openSession();
    }

    private Session session = getSession();
    private Transaction tx;
    private Connection con;

    private String qry;
    private PreparedStatement pstmt;
    private ResultSet rs;

    public Commands(Connection con) {
        this.con = con;
    }

    public void add(String username, String password, String name, String position, String section) {
        tx = session.beginTransaction();
        User user = new User();
        user.setSection(section);
        user.setUsername(username);
        user.setPassword(password);
        user.setName(name);
        user.setPosition(position);
        session.save(user);
        tx.commit();
    }

    public void update(User user, String username, String password, String name, String position, String section) {
        delete(user);
        add(username, password, name, position, section);
    }

    public void delete(User user) {
        tx = session.beginTransaction();
        session.delete(user);
        tx.commit();
    }

    public void delete(Report report) {
        tx = session.beginTransaction();
        session.delete(report);
        tx.commit();
    }

    public void upload(User user, InputStream isblob, String ay, int size, String filename) {
        tx = session.beginTransaction();
        Report report = new Report();
        report.setFilename(filename);
        report.setSection(user.getSection());
        report.setRevise(false);
        report.setAY(ay);
        Blob blob = Hibernate.getLobCreator(session).createBlob(isblob, size);
        report.setFile(blob);
        report.setTimestamp(new Timestamp(new java.util.Date().getTime()));
        session.save(report);
        tx.commit();
    }

    public void requestRevise(Report report) {
        tx = session.beginTransaction();
        report.setRevise(true);
        session.update(report);
        tx.commit();
    }

    public void updatePassword(User user, String password) {
        tx = session.beginTransaction();
        user.setPassword(password);
        session.update(user);
        tx.commit();
    }

    public void revise(Report report, User user, InputStream isblob, String ay, int size, String filename) {

        tx = session.beginTransaction();
        report.setFilename(filename);
        report.setSection(user.getSection());
        report.setAY(ay);
        Blob blob = Hibernate.getLobCreator(session).createBlob(isblob, size);
        report.setFile(blob);
        report.setTimestamp(new Timestamp(new java.util.Date().getTime()));
        report.setRevise(false);
        session.update(report);
        tx.commit();

    }
    public boolean exists(String username,User user) {
        try {
            qry = "SELECT * FROM USER WHERE USERNAME = (?)";
            pstmt = con.prepareStatement(qry);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();
            if (!rs.next()) {
                return false;
            }
        } catch (SQLException e) {
        }
        if(user!=null&&user.getUsername().equals(username))
            return false;
        return true;
    }

    public String getAY(InputStream is, String ext) {
        try {
            Workbook temp = null;
            if (ext.equals("xls")) {
                temp = new HSSFWorkbook(is);
            } else if (ext.equals("xlsx")) {
                temp = new XSSFWorkbook(is);
            }
            Sheet sheet = temp.getSheetAt(0);
            Row row = sheet.getRow(3);
            return row.getCell(row.getFirstCellNum()).getStringCellValue().split("A.Y.")[1].trim();
        } catch (IOException e) {
        }
        return null;
    }

    public void download(OutputStream os, Report report) {
        try {
            InputStream is = report.getFile().getBinaryStream();
            byte[] bytes = new byte[(int) report.getFile().length()];
            int i = 0;
            while ((i = is.read(bytes)) != -1) {
                os.write(bytes, 0, i);
            }
            os.flush();
            os.close();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public void log(User user, String action) {
        tx = session.beginTransaction();
        Log log = new Log();
        log.setSection(user.getSection());
        log.setAction(action);
        log.setTimestamp(new Timestamp(new java.util.Date().getTime()));
        session.save(log);
        tx.commit();
    }

    // Get
    public User getUser(String username, String password) {
        try {
            qry = "SELECT * FROM USER WHERE USERNAME = (?) AND PASSWORD = (?)";
            pstmt = con.prepareStatement(qry);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            rs = pstmt.executeQuery();
            if (!rs.next()) {
                return null;
            }
            User tmp = new User();
            tmp.setUsername(rs.getString("username"));
            tmp.setPassword(rs.getString("password"));
            tmp.setName(rs.getString("name"));
            tmp.setPosition(rs.getString("position"));
            tmp.setSection(rs.getString("section"));
            return tmp;
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return null;
    }

    public User getUser(String section) {
        try {
            qry = "SELECT * FROM USER WHERE SECTION = (?)";
            pstmt = con.prepareStatement(qry);
            pstmt.setString(1, section);
            rs = pstmt.executeQuery();
            rs.next();
            User tmp = new User();
            tmp.setUsername(rs.getString("username"));
            tmp.setPassword(rs.getString("password"));
            tmp.setName(rs.getString("name"));
            tmp.setPosition(rs.getString("position"));
            tmp.setSection(rs.getString("section"));
            return tmp;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<User> getUsers(String section) {
        try {
            List<User> users = new ArrayList();
            qry = "SELECT * FROM USER WHERE SECTION != (?) ORDER BY SECTION ASC";
            pstmt = con.prepareStatement(qry);
            pstmt.setString(1, section);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                User tmp = new User();
                tmp.setName(rs.getString("name"));
                tmp.setPassword(rs.getString("password"));
                tmp.setPosition(rs.getString("position"));
                tmp.setSection(rs.getString("section"));
                tmp.setUsername(rs.getString("username"));
                users.add(tmp);
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<User> getUsers(String key, String section) {
        try {
            List<User> users = new ArrayList();
            qry = "SELECT * FROM USER WHERE (UPPER(USERNAME) LIKE UPPER(?) OR UPPER(NAME) LIKE UPPER(?) OR UPPER(POSITION) LIKE UPPER(?) OR UPPER(SECTION) LIKE UPPER(?)) AND SECTION != (?) ORDER BY SECTION ASC";
            pstmt = con.prepareStatement(qry);
            pstmt.setString(1, "%" + key + "%");
            pstmt.setString(2, "%" + key + "%");
            pstmt.setString(3, "%" + key + "%");
            pstmt.setString(4, "%" + key + "%");
            pstmt.setString(5, section);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                User tmp = new User();
                tmp.setName(rs.getString("name"));
                tmp.setPassword(rs.getString("password"));
                tmp.setPosition(rs.getString("position"));
                tmp.setSection(rs.getString("section"));
                tmp.setUsername(rs.getString("username"));
                users.add(tmp);
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Report getReport(int id) {
        try {
            qry = "SELECT * FROM REPORT WHERE ID = (?)";
            pstmt = con.prepareStatement(qry);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            rs.next();
            Report tmp = new Report();
            tmp.setId(rs.getInt("id"));
            tmp.setFilename(rs.getString("filename"));
            tmp.setTimestamp(rs.getTimestamp("timestamp"));
            tmp.setRevise(rs.getBoolean("revise"));
            tmp.setFile(rs.getBlob("file"));
            tmp.setSection(rs.getString("section"));
            tmp.setAY(rs.getString("ay"));
            return tmp;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Report> getReports(User user) {

        try {
            List<Report> reports = new ArrayList();
            if (user.getPosition().equals("Section Head")) {
                qry = "SELECT * FROM REPORT WHERE SECTION = (?) OR ID = 1";
            } else if (user.getPosition().equals("Clerk")) {
                qry = "SELECT * FROM REPORT";
            }
            pstmt = con.prepareStatement(qry);
            if (user.getPosition().equals("Section Head")) {
                pstmt.setString(1, user.getSection());
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Report tmp = new Report();
                tmp.setId(rs.getInt("id"));
                tmp.setFilename(rs.getString("filename"));
                tmp.setTimestamp(rs.getTimestamp("timestamp"));
                tmp.setRevise(rs.getBoolean("revise"));
                tmp.setFile(rs.getBlob("file"));
                tmp.setAY(rs.getString("ay"));
                tmp.setSection(rs.getString("section"));
                reports.add(tmp);
            }
            return reports;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Report> getReports(String key, User user) {
        try {
            List<Report> reports = new ArrayList();
            if (user.getPosition().equals("Section Head")) {
                qry = "SELECT * FROM REPORT WHERE (UPPER(FILENAME) LIKE UPPER(?) OR UPPER(TIMESTAMP) LIKE UPPER(?) OR UPPER(SECTION) LIKE UPPER(?)) AND (SECTION = (?) OR SECTION = 'N/A')";
            } else if (user.getPosition().equals("Clerk")) {
                qry = "SELECT * FROM REPORT WHERE (UPPER(FILENAME) LIKE UPPER(?) OR UPPER(TIMESTAMP) LIKE UPPER(?) OR UPPER(SECTION) LIKE UPPER(?))";
            }
            pstmt = con.prepareStatement(qry);
            pstmt.setString(1, "%" + key + "%");
            pstmt.setString(2, "%" + key + "%");
            pstmt.setString(3, "%" + key + "%");
            if (user.getPosition().equals("Section Head")) {
                pstmt.setString(4, user.getSection());
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Report tmp = new Report();
                tmp.setId(rs.getInt("id"));
                tmp.setFilename(rs.getString("filename"));
                tmp.setTimestamp(rs.getTimestamp("timestamp"));
                tmp.setRevise(rs.getBoolean("revise"));
                tmp.setFile(rs.getBlob("file"));
                tmp.setSection(rs.getString("section"));
                tmp.setAY(rs.getString("ay"));
                reports.add(tmp);
            }
            return reports;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Report> getSReports(String section) {
        try {
            List<Report> reports = new ArrayList();
            int year = Calendar.getInstance().get(Calendar.YEAR);
            qry = "SELECT * FROM REPORT WHERE (UPPER(SECTION) LIKE UPPER(?) AND UPPER(AY) LIKE UPPER(?))";
            pstmt = con.prepareStatement(qry);
            pstmt.setString(1, "%" + section + "%");
            pstmt.setString(2, "%" + (year - 1) + "-" + (year) + "%");
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Report tmp = new Report();
                tmp.setId(rs.getInt("id"));
                tmp.setFilename(rs.getString("filename"));
                tmp.setTimestamp(rs.getTimestamp("timestamp"));
                tmp.setRevise(rs.getBoolean("revise"));
                tmp.setFile(rs.getBlob("file"));
                tmp.setSection(rs.getString("section"));
                tmp.setAY(rs.getString("ay"));
                reports.add(tmp);
            }
            return reports;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Log> getLogs() {
        try {
            List<Log> logs = new ArrayList();
            qry = "SELECT * FROM LOG ORDER BY ID DESC";
            pstmt = con.prepareStatement(qry);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Log tmp = new Log();
                tmp.setId(rs.getInt("id"));
                tmp.setAction(rs.getString("action"));
                tmp.setTimestamp(rs.getTimestamp("timestamp"));
                tmp.setSection(rs.getString("section"));
                logs.add(tmp);
            }
            return logs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Log> getLogs(String key) {
        try {
            List<Log> logs = new ArrayList();
            qry = "SELECT * FROM LOG WHERE (UPPER(ACTION) LIKE UPPER(?) OR TIMESTAMP LIKE (?) OR UPPER(SECTION) LIKE UPPER(?)) ORDER BY ID DESC";
            pstmt = con.prepareStatement(qry);
            pstmt.setString(1, "%" + key + "%");
            pstmt.setString(2, "%" + key + "%");
            pstmt.setString(3, "%" + key + "%");
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Log tmp = new Log();
                tmp.setId(rs.getInt("id"));
                tmp.setAction(rs.getString("action"));
                tmp.setTimestamp(rs.getTimestamp("timestamp"));
                tmp.setSection(rs.getString("section"));
                logs.add(tmp);
            }
            return logs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean hasDigit(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (Character.isDigit(s.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public boolean isNumeric(Cell cell) {
        try {
            double x = cell.getNumericCellValue();
            return x != 0;
        } catch (IllegalStateException e) {
            return false;
        }
    }

    public String getColheader(int col, int sheet) {
        try {
            String header[] = null;
            if (sheet == 0 || sheet == 1) {
                header = new String[]{"", "Existing Titles", "Acquired Titles", "Total", "Existing Volumes", "Acquired Volumes", "Total", "Missing", "Recovered", "Outright Disposal", "For Donation", "Transferred to", "Received from", "Total"};
            } else if (sheet == 2) {
                header = new String[]{"", "Circulation", "Civil Law", "Ecclesiastical", "EHS - Grade School", "Filipiniana", "Gen. Ref", "Graduate School", "Health Sciences", "Heritage", "EHS - High School", "Humanities", "Internet", "Music", "Old Books", "Religion", "Science and Technology", "Serials", "Social Science", "Spanish", "Total"};
            } else if (sheet == 3) {
                header = new String[]{"", "Library User", "Library User", "Library User", "Internet User", "Internet User", "Internet User", "MS Office User", "MS Office User", "MS Office User", "CD-ROM User", "CD-ROM User", "CD-ROM User"};
            }
            return header[col];
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        return null;
    }

    public void setFormula(Cell cell) {
        try {
            if (!cell.getCellFormula().trim().equals("")) {
                cell.setCellFormula(cell.getCellFormula());
            }
        } catch (IllegalStateException e) {
        }
    }
   XSSFWorkbook temp;

    public void generate(String section[], InputStream is, OutputStream os) throws IOException {
        try {
            long stime,etime;
            stime = System.currentTimeMillis();
            temp = new XSSFWorkbook(is);
            
            
            for (int i = 0; i < section.length; i++) {
                List<Report> tmp = getSReports(section[i]);
                read(section[i], tmp);
            }
            try {
                temp.getCreationHelper().createFormulaEvaluator().evaluateAll();
            } catch (IllegalArgumentException e) {
            }
            temp.write(os);
            temp.close();
            etime = System.currentTimeMillis();
            System.out.println((etime-stime)/1000+" seconds");
        } catch (SQLException e) {
        }
    }

    public void read(String section, List<Report> reports) throws IOException, SQLException {
        Sheet sheet = null;
        Workbook wb = null;
        Iterator<Cell> cellitr;
        Cell cell;
        Row row;
        String tmp[];
        for (int i = 0; i < reports.size(); i++) {
            String ext = reports.get(i).getFilename().split("\\.")[1];
            if (ext.equals("xls")) {
                wb = new HSSFWorkbook(reports.get(i).getFile().getBinaryStream());
            } else if (ext.equals("xlsx")) {
                wb = new XSSFWorkbook(reports.get(i).getFile().getBinaryStream());
            }
            sheet = wb.getSheetAt(0);
            Iterator<Row> itrwb = sheet.iterator();
            String rowheader = "", rowheaderprime = "", colheader = "";
            //Collection
            while (itrwb.hasNext()) {
                row = itrwb.next();
                cellitr = row.cellIterator();
                int col = 0;
                while (cellitr.hasNext()) {
                    cell = cellitr.next();
                    rowheader = row.getCell(row.getFirstCellNum()).getStringCellValue();
                    colheader = getColheader(col++, 0);
                    tmp = rowheader.split(" ");
                    if (tmp[0].length() == 4 && hasDigit(tmp[0])) {
                        rowheaderprime = rowheader;
                    }
                    if (isNumeric(cell)) {
                        write(section, rowheader, rowheaderprime, colheader, cell.getNumericCellValue(), 0);
                    }
                }
            }
            //Missing
            sheet = wb.getSheetAt(1);
            itrwb = sheet.iterator();
            while (itrwb.hasNext()) {
                row = itrwb.next();
                cellitr = row.cellIterator();
                while (cellitr.hasNext()) {
                    cell = cellitr.next();
                    rowheader = row.getCell(row.getFirstCellNum()).getStringCellValue();
                    tmp = rowheader.trim().split(" ");
                    if (tmp[0].length() == 4 && hasDigit(tmp[0])) {
                        colheader = tmp[1];
                    }
                    if (isNumeric(cell)) {
                        write(section, "", "", colheader, cell.getNumericCellValue(), 1);
                    }
                }
            }
            //Library util
            sheet = wb.getSheetAt(2);
            itrwb = sheet.iterator();
            while (itrwb.hasNext()) {
                row = itrwb.next();
                cellitr = row.cellIterator();
                while (cellitr.hasNext()) {
                    cell = cellitr.next();
                    rowheader = row.getCell(row.getFirstCellNum()).getStringCellValue();
                    tmp = rowheader.split(" ");
                    if (tmp[0].length() == 2 && hasDigit(tmp[0])) {
                        rowheaderprime = rowheader;
                    }
                    if (isNumeric(cell)) {
                        write(section, rowheader, rowheaderprime, "", cell.getNumericCellValue(), 2);
                    }
                }
            }

            //Library Util and Services
            sheet = wb.getSheetAt(3);
            itrwb = sheet.iterator();
            while (itrwb.hasNext()) {
                row = itrwb.next();
                cellitr = row.cellIterator();
                int col = 0;
                while (cellitr.hasNext()) {
                    cell = cellitr.next();
                    rowheader = row.getCell(row.getFirstCellNum()).getStringCellValue();
                    tmp = rowheader.split(" ");
                    colheader = getColheader(col++, 3);
                    if (isNumeric(cell)) {
                        if (colheader.equals("Library User")) {
                            write(section, rowheader, "", colheader, cell.getNumericCellValue(), 3);
                        } else if (colheader.equals("Internet User")) {
                            write(section, rowheader, "", colheader, cell.getNumericCellValue(), 4);
                        }
                    }
                }
            }

            wb.close();
        }
    }

    public void write(String section, String rowheader, String rowheaderprime, String colheader, double value, int s) throws IOException {
        Sheet sheet = null;
        String temprowheader = "", temprowheaderprime = "", tempcolheader = "";
        Iterator<Row> itrtemp;
        Iterator<Cell> tempcell;
        Row temprow;
        Cell cell;
        String tmp[];
        boolean match = false;
        if (s == 0 || s == 1) {
            sheet = temp.getSheetAt(0);
            itrtemp = sheet.iterator();
            while (itrtemp.hasNext() && !match) {
                temprow = itrtemp.next();
                tempcell = temprow.cellIterator();
                int col = 0;
                while (tempcell.hasNext() && !match) {
                    cell = tempcell.next();
                    setFormula(cell);
                    temprowheader = temprow.getCell(temprow.getFirstCellNum()).getStringCellValue();
                    tempcolheader = getColheader(col++, s);
                    tmp = temprowheader.split(" ");
                    if (s == 0) {
                        if (tmp[0].length() == 4 && hasDigit(tmp[0])) {
                            temprowheaderprime = temprowheader;
                        }
                        if (tempcolheader != null) {
                            if (!temprowheaderprime.trim().equals("") && !temprowheader.trim().equals("") && !tempcolheader.trim().equals("")) {
                                if (rowheaderprime.contains(temprowheaderprime) && (rowheader.contains(temprowheader) || section.contains(temprowheader)) && colheader.contains(tempcolheader)) {
                                    if (!rowheaderprime.equals(temprowheader)) {
                                        cell.setCellValue(cell.getNumericCellValue() + value);
                                        match = true;
                                    }
                                }
                            }
                        }
                    } else if (s == 1) {
                        if (tempcolheader != null) {
                            if (!temprowheader.trim().equals("") && !colheader.trim().equals("")) {
                                if (section.contains(temprowheader) && tempcolheader.contains(colheader)) {
                                    cell.setCellValue(cell.getNumericCellValue() + value);
                                    match = true;
                                }
                            }
                        }
                    }
                }
            }
        }
        if (s == 2) {
            sheet = temp.getSheetAt(1);
            itrtemp = sheet.iterator();
            while (itrtemp.hasNext() && !match) {
                temprow = itrtemp.next();
                tempcell = temprow.cellIterator();
                int col = 0;
                while (tempcell.hasNext() && !match) {
                    cell = tempcell.next();
                    setFormula(cell);
                    temprowheader = temprow.getCell(temprow.getFirstCellNum()).getStringCellValue();
                    tempcolheader = getColheader(col++, s);
                    tmp = temprowheader.split(" ");
                    if (s == 2) {
                        if (tmp[0].length() == 2 && hasDigit(tmp[0])) {
                            temprowheaderprime = temprowheader;
                        }
                        if (tempcolheader != null) {
                            if (!temprowheaderprime.trim().equals("") && !temprowheader.trim().equals("") && !tempcolheader.trim().equals("")) {
                                if (rowheaderprime.contains(temprowheaderprime) && rowheader.contains(temprowheader) && section.contains(tempcolheader)) {
                                    if (!rowheaderprime.equals(temprowheader)) {
                                        cell.setCellValue(cell.getNumericCellValue() + value);
                                        match = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (s == 3 || s == 4) {
            if (s == 3) {
                sheet = temp.getSheetAt(2);
            } else if (s == 4) {
                sheet = temp.getSheetAt(3);
            }
            itrtemp = sheet.iterator();
            while (itrtemp.hasNext() && !match) {
                temprow = itrtemp.next();
                tempcell = temprow.cellIterator();
                int col = 0;
                while (tempcell.hasNext() && !match) {
                    cell = tempcell.next();
                    setFormula(cell);
                    temprowheader = temprow.getCell(temprow.getFirstCellNum()).getStringCellValue();
                    tempcolheader = getColheader(col++, 2);
                    if (tempcolheader != null) {
                        if (!temprowheader.trim().equals("") && !tempcolheader.trim().equals("")) {
                            if (rowheader.contains(temprowheader) && section.contains(tempcolheader)) {
                                cell.setCellValue(cell.getNumericCellValue() + value);
                                match = true;
                            }
                        }
                    }
                }
            }
        }
    }
}
