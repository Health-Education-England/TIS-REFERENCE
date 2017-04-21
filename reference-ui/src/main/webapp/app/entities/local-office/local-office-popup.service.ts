import {Injectable, Component} from "@angular/core";
import {Router} from "@angular/router";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {LocalOffice} from "./local-office.model";
import {LocalOfficeService} from "./local-office.service";
@Injectable()
export class LocalOfficePopupService {
	private isOpen = false;

	constructor(private modalService: NgbModal,
				private router: Router,
				private localOfficeService: LocalOfficeService) {
	}

	open(component: Component, id?: number | any): NgbModalRef {
		if (this.isOpen) {
			return;
		}
		this.isOpen = true;

		if (id) {
			this.localOfficeService.find(id).subscribe(localOffice => {
				this.localOfficeModalRef(component, localOffice);
			});
		} else {
			return this.localOfficeModalRef(component, new LocalOffice());
		}
	}

	localOfficeModalRef(component: Component, localOffice: LocalOffice): NgbModalRef {
		let modalRef = this.modalService.open(component, {size: 'lg', backdrop: 'static'});
		modalRef.componentInstance.localOffice = localOffice;
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
