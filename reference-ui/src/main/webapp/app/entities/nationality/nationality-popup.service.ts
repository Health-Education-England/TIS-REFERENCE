import {Injectable, Component} from "@angular/core";
import {Router} from "@angular/router";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {Nationality} from "./nationality.model";
import {NationalityService} from "./nationality.service";
@Injectable()
export class NationalityPopupService {
	private isOpen = false;

	constructor(private modalService: NgbModal,
				private router: Router,
				private nationalityService: NationalityService) {
	}

	open(component: Component, id?: number | any): NgbModalRef {
		if (this.isOpen) {
			return;
		}
		this.isOpen = true;

		if (id) {
			this.nationalityService.find(id).subscribe(nationality => {
				this.nationalityModalRef(component, nationality);
			});
		} else {
			return this.nationalityModalRef(component, new Nationality());
		}
	}

	nationalityModalRef(component: Component, nationality: Nationality): NgbModalRef {
		let modalRef = this.modalService.open(component, {size: 'lg', backdrop: 'static'});
		modalRef.componentInstance.nationality = nationality;
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