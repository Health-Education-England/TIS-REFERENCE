import {Injectable, Component} from "@angular/core";
import {Router} from "@angular/router";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {SexualOrientation} from "./sexual-orientation.model";
import {SexualOrientationService} from "./sexual-orientation.service";
@Injectable()
export class SexualOrientationPopupService {
	private isOpen = false;

	constructor(private modalService: NgbModal,
				private router: Router,
				private sexualOrientationService: SexualOrientationService) {
	}

	open(component: Component, id?: number | any): NgbModalRef {
		if (this.isOpen) {
			return;
		}
		this.isOpen = true;

		if (id) {
			this.sexualOrientationService.find(id).subscribe(sexualOrientation => {
				this.sexualOrientationModalRef(component, sexualOrientation);
			});
		} else {
			return this.sexualOrientationModalRef(component, new SexualOrientation());
		}
	}

	sexualOrientationModalRef(component: Component, sexualOrientation: SexualOrientation): NgbModalRef {
		let modalRef = this.modalService.open(component, {size: 'lg', backdrop: 'static'});
		modalRef.componentInstance.sexualOrientation = sexualOrientation;
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
