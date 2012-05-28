package net.thornydev.mybatis.koan.koan19;

import static org.junit.Assert.*;

import java.io.InputStream;

import net.thornydev.mybatis.koan.domain.Address;
import net.thornydev.mybatis.koan.domain.Staff;
import net.thornydev.mybatis.koan.util.Email;
import net.thornydev.mybatis.koan.util.NullEmail;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class Koan19 {

	static SqlSession session;
	static SqlSessionFactory sessionFactory;
	static Koan19Mapper mapper;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		String resource = "net/thornydev/mybatis/koan/koan19/koan19-config.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		sessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		inputStream.close();

		session = sessionFactory.openSession();
		mapper = session.getMapper(Koan19Mapper.class);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		if (session != null) session.close();
	}

	@Before
	public void setUp() throws Exception {
		mapper.setEmail(null, 2);
	}

	@After
	public void tearDown() throws Exception {
		// restore it back to what it was
		mapper.setEmail("Jon.Stephens@sakilastaff.com", 2);  // FIXME: maybe don't need to do this if do a session rollback?
	}

	@Test
	public void learnToUseTypeHandlerAsFactory_firstStaffMemberShouldHaveNonNullEmail() {
		Staff st = session.selectOne("getStaffById", 1);
		assertNotNull(st);
		assertEquals(1, st.getId());
		assertEquals("Mike", st.getFirstName());
		assertTrue(st.isActive());
		assertEquals("Mike", st.getUsername());
		
		Address addr = st.getAddress(); 
		assertNotNull(addr);
		assertEquals(3, addr.getId().intValue());
		assertEquals("Alberta", addr.getDistrict());
		
		Email e = st.getEmail();
		assertNotNull(e);
		assertFalse(e instanceof NullEmail);
		assertEquals("Mike.Hillyer", e.getUsername());
	}

	@Test
	public void secondStaffMemberShouldHaveNullEmail() {
		Staff st = session.selectOne("getStaffById", 2);
		assertNotNull(st);
		assertEquals(2, st.getId());		
		assertEquals("Jon", st.getUsername());
				
		Address addr = st.getAddress(); 
		assertNotNull(addr);
		assertEquals(4, addr.getId().intValue());
		assertEquals("QLD", addr.getDistrict());
		
		Email e = st.getEmail();
		assertNotNull(e);
		assertTrue(e instanceof NullEmail);
		assertEquals("", e.getUsername());		
	}	
}