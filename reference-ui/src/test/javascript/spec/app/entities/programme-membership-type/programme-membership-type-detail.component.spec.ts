import {ComponentFixture, TestBed, async} from "@angular/core/testing";
import {MockBackend} from "@angular/http/testing";
import {Http, BaseRequestOptions} from "@angular/http";
import {OnInit} from "@angular/core";
import {DatePipe} from "@angular/common";
import {ActivatedRoute} from "@angular/router";
import {Observable} from "rxjs/Rx";
import {DateUtils, DataUtils, JhiLanguageService} from "ng-jhipster";
import {MockLanguageService} from "../../../helpers/mock-language.service";
import {MockActivatedRoute} from "../../../helpers/mock-route.service";
import {ProgrammeMembershipTypeDetailComponent} from "../../../../../../main/webapp/app/entities/programme-membership-type/programme-membership-type-detail.component";
import {ProgrammeMembershipTypeService} from "../../../../../../main/webapp/app/entities/programme-membership-type/programme-membership-type.service";
import {ProgrammeMembershipType} from "../../../../../../main/webapp/app/entities/programme-membership-type/programme-membership-type.model";

describe('Component Tests', () => {

	describe('ProgrammeMembershipType Management Detail Component', () => {
		let comp: ProgrammeMembershipTypeDetailComponent;
		let fixture: ComponentFixture<ProgrammeMembershipTypeDetailComponent>;
		let service: ProgrammeMembershipTypeService;

		beforeEach(async(() => {
			TestBed.configureTestingModule({
				declarations: [ProgrammeMembershipTypeDetailComponent],
				providers: [
					MockBackend,
					BaseRequestOptions,
					DateUtils,
					DataUtils,
					DatePipe,
					{
						provide: ActivatedRoute,
						useValue: new MockActivatedRoute({id: 123})
					},
					{
						provide: Http,
						useFactory: (backendInstance: MockBackend, defaultOptions: BaseRequestOptions) => {
							return new Http(backendInstance, defaultOptions);
						},
						deps: [MockBackend, BaseRequestOptions]
					},
					{
						provide: JhiLanguageService,
						useClass: MockLanguageService
					},
					ProgrammeMembershipTypeService
				]
			}).overrideComponent(ProgrammeMembershipTypeDetailComponent, {
				set: {
					template: ''
				}
			}).compileComponents();
		}));

		beforeEach(() => {
			fixture = TestBed.createComponent(ProgrammeMembershipTypeDetailComponent);
			comp = fixture.componentInstance;
			service = fixture.debugElement.injector.get(ProgrammeMembershipTypeService);
		});


		describe('OnInit', () => {
			it('Should call load all on init', () => {
				// GIVEN

				spyOn(service, 'find').and.returnValue(Observable.of(new ProgrammeMembershipType(10)));

				// WHEN
				comp.ngOnInit();

				// THEN
				expect(service.find).toHaveBeenCalledWith(123);
				expect(comp.programmeMembershipType).toEqual(jasmine.objectContaining({id: 10}));
			});
		});
	});

});
