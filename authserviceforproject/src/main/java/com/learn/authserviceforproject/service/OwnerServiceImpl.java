//package com.learn.authserviceforproject.service;
//
//
//
//import java.util.List;
//import java.util.Optional;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.learn.authserviceforproject.exceptions.EmailIdAlreadyExistException;
//import com.learn.authserviceforproject.exceptions.EmailIdNotExistException;
//import com.learn.authserviceforproject.model.Owner;
//import com.learn.authserviceforproject.repository.OwnerRepository;
//
//@Service
//public class OwnerServiceImpl  implements OwnerService{
//	
//	@Autowired
//	private OwnerRepository  ownerRepository;
//
//	@Override
//	public Owner registerOwner(Owner owner) throws EmailIdAlreadyExistException {
//		Owner  savedUser=null;
//		String mailId =owner.getEmailId();
//		if (ownerRepository.existsById(mailId)) {
//			throw  new  EmailIdAlreadyExistException("EmailId Already Exist...");
//		}
//		
//		return ownerRepository.save(owner);
////		else {
////			savedUser =ownerRepository.save(owner);
////		}
////		 
////		return savedUser;
//	}
//
//	@Override
//	public Owner getUserByEmailId(String emailId) throws EmailIdNotExistException {
//		 Optional<Owner>  optional = ownerRepository.findByEmailId(emailId);
//		 if(optional.isEmpty()) {
//			 throw new EmailIdNotExistException("Invalid Email Id");
//		 }
//		 return  optional.get();
//		 
//	}
//
//	@Override
//	public Owner updateOwner(Owner owner) throws EmailIdNotExistException {
//		 Optional<Owner>  optional = ownerRepository.findById(owner.getEmailId());
//       if (optional.isEmpty()) {
//    	   throw new EmailIdNotExistException("Invalid owner Email Id");
//       }
//       Owner  owner1 = optional.get();
//       if (isValid(owner.getPassword())) {
//    	   owner1.setPassword(owner.getPassword());
//       }
//      if(isValid(owner.getOwnername())) {
//    	  owner1.setOwnername(owner.getOwnername());
//      }
//      return  ownerRepository.save(owner1)  ;
//	}
//	
//	private   boolean isValid(String value) {
//	return  (value!=null && !value.isBlank())?true:false;
//	}
//
//	@Override
//	public boolean deleteOwner(String emailId) {
//		 Optional<Owner>  optional = ownerRepository.findById(emailId);
//	    boolean isDeleted =false;
//		 if (optional.isPresent()) {
//			 	ownerRepository.deleteById(emailId);
//			 	isDeleted =true;
//		 }
//		 return isDeleted;
//            
//	}
//
//	@Override
//	public List<Owner> getAllOwner(){
//			return ownerRepository.findAll();
//	}
//
//	@Override
//	public boolean validateOwner(Owner owner) {
//		Optional<Owner> optional = ownerRepository.findByEmailIdAndPassword(owner.getEmailId(), owner.getPassword());
//	  //retrun optional.isPresent()?true:false;
//		return  optional.isPresent();
//		
//	}
//	
//	
//	
//}
// 
