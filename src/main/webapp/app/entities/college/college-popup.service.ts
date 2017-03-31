import {Injectable, Component} from "@angular/core";
import {Router} from "@angular/router";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {College} from "./college.model";
import {CollegeService} from "./college.service";
@Injectable()
export class CollegePopupService {
	private isOpen = false;

	constructor(private modalService: NgbModal,
				private router: Router,
				private collegeService: CollegeService) {
	}

	open(component: Component, id?: number | any): NgbModalRef {
		if (this.isOpen) {
			return;
		}
		this.isOpen = true;

		if (id) {
			this.collegeService.find(id).subscribe(college => {
				this.collegeModalRef(component, college);
			});
		} else {
			return this.collegeModalRef(component, new College());
		}
	}

	collegeModalRef(component: Component, college: College): NgbModalRef {
		let modalRef = this.modalService.open(component, {size: 'lg', backdrop: 'static'});
		modalRef.componentInstance.college = college;
		modalRef.result.then(result => {
			this.router.navigate([{outlets: {popup: null}}], {replaceUrl: true});
			this.isOpen = false;
		}, (reason) => {
			this.router.navigate([{outlets: {popup: null}}], {replaceUrl: true});
			this.isOpen = false;
		});
		return modalRef;
	}
}
